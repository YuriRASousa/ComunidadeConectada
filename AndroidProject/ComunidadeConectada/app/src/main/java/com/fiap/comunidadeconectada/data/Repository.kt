package com.fiap.comunidadeconectada.data

import com.fiap.comunidadeconectada.api.ApiService
import com.fiap.comunidadeconectada.api.models.CreateResourceRequest
import com.fiap.comunidadeconectada.api.models.LoginRequest
import com.fiap.comunidadeconectada.api.models.RegisterRequest
import com.fiap.comunidadeconectada.api.models.SendMessageRequest
import com.fiap.comunidadeconectada.data.dao.EventDao
import com.fiap.comunidadeconectada.data.dao.MessageDao
import com.fiap.comunidadeconectada.data.dao.PostDao
import com.fiap.comunidadeconectada.data.dao.RatingDao
import com.fiap.comunidadeconectada.data.dao.ResourceDao
import com.fiap.comunidadeconectada.data.dao.TransactionDao
import com.fiap.comunidadeconectada.data.dao.UserDao
import com.fiap.comunidadeconectada.data.entities.MessageEntity
import com.fiap.comunidadeconectada.data.entities.ResourceEntity
import com.fiap.comunidadeconectada.data.entities.UserEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

import kotlinx.coroutines.flow.first

class Repository(
    private val apiService: ApiService,
    private val preferencesManager: PreferencesManager,
    private val userDao: UserDao,
    private val resourceDao: ResourceDao,
    private val messageDao: MessageDao,
    private val ratingDao: RatingDao,
    private val transactionDao: TransactionDao,
    private val eventDao: EventDao,
    private val postDao: PostDao
) {

    // ============ AUTENTICAÇÃO ============
    suspend fun login(email: String, password: String) {
        try {
            val response = apiService.login(LoginRequest(email, password))
            if (response.success && response.data != null) {
                preferencesManager.saveAuthToken(response.data.token)
                preferencesManager.saveUserId(response.data.user.id)
                preferencesManager.saveUserName(response.data.user.name)
                preferencesManager.saveUserEmail(response.data.user.email)
                preferencesManager.setLoggedIn(true)

                // Salvar usuário localmente
                val userEntity = UserEntity(
                    id = response.data.user.id,
                    name = response.data.user.name,
                    email = response.data.user.email,
                    address = response.data.user.address,
                    profileImageUrl = response.data.user.profileImageUrl,
                    reputation = response.data.user.reputation,
                    totalTransactions = response.data.user.totalTransactions,
                    isVerified = response.data.user.isVerified,
                    createdAt = response.data.user.createdAt
                )
                userDao.insert(userEntity)
            }
        } catch (e: Exception) {
            throw Exception("Erro ao fazer login: ${e.message}")
        }
    }

    suspend fun register(name: String, email: String, password: String, address: String) {
        try {
            val response = apiService.register(
                RegisterRequest(name, email, password, address)
            )
            if (response.success && response.data != null) {
                val userEntity = UserEntity(
                    id = response.data.id,
                    name = response.data.name,
                    email = response.data.email,
                    address = response.data.address,
                    profileImageUrl = response.data.profileImageUrl,
                    reputation = response.data.reputation,
                    totalTransactions = response.data.totalTransactions,
                    isVerified = response.data.isVerified,
                    createdAt = response.data.createdAt
                )
                userDao.insert(userEntity)
            }
        } catch (e: Exception) {
            throw Exception("Erro ao registrar: ${e.message}")
        }
    }

    suspend fun logout() {
        preferencesManager.clearAll()
    }

    // ============ USUÁRIOS ============
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getCurrentUser(): Flow<UserEntity?> {
        return preferencesManager.getUserId().flatMapLatest { userId ->
            if (userId != null) {
                userDao.getUserByIdFlow(userId)
            } else {
                flowOf(null)
            }
        }
    }

    // ============ RECURSOS ============
    fun getAllResources(): Flow<List<ResourceEntity>> {
        return resourceDao.getAllActiveResources()
    }

    suspend fun getResourceById(resourceId: String): ResourceEntity? {
        return resourceDao.getResourceById(resourceId)
    }

    suspend fun createResource(title: String, description: String, category: String) {
        try {
            val userId = preferencesManager.getUserId().first() ?: "user_default"
            val newResource = ResourceEntity(
                id = System.currentTimeMillis().toString(),
                title = title,
                description = description,
                category = category,
                condition = "Bom",
                offerantId = userId,
                availability = "Disponível",
                type = "Empréstimo"
            )
            
            // Salvar no Room (Offline-first)
            resourceDao.insert(newResource)

            // Tentar enviar para a API
            val token = preferencesManager.getAuthToken().first()
            if (token != null) {
                val request = CreateResourceRequest(
                    title = title,
                    description = description,
                    category = category,
                    condition = "Bom",
                    availability = "Disponível",
                    type = "Empréstimo",
                    latitude = null,
                    longitude = null
                )
                apiService.createResource(request, "Bearer $token")
            }
        } catch (e: Exception) {
            // Em caso de erro na rede, o dado já está no Room
            println("Erro ao sincronizar com API: ${e.message}")
        }
    }

    fun getResourcesByCategory(category: String): Flow<List<ResourceEntity>> {
        return resourceDao.getResourcesByCategory(category)
    }

    suspend fun getUserNameById(userId: String): String? {
        return resourceDao.getUserNameById(userId)
    }

    // ============ MENSAGENS ============
    fun getResourceMessages(resourceId: String): Flow<List<MessageEntity>> {
        return messageDao.getMessagesByResource(resourceId)
    }

    suspend fun sendMessage(resourceId: String, content: String) {
        val userId = preferencesManager.getUserId().first() ?: "user_id_default"
        
        // Garantir que o usuário existe localmente antes de enviar mensagem (devido à FK no banco)
        if (userDao.getUserById(userId) == null) {
            userDao.insert(
                UserEntity(
                    id = userId,
                    name = "Usuário Local",
                    email = "usuario@email.com",
                    address = "Rua da Comunidade, 123"
                )
            )
        }

        val newMessage = MessageEntity(
            id = System.currentTimeMillis().toString(),
            senderId = userId,
            resourceId = resourceId,
            content = content,
            timestamp = System.currentTimeMillis()
        )
        
        // Salva localmente primeiro (Offline-first)
        messageDao.insert(newMessage)

        try {
            val token = preferencesManager.getAuthToken().first()
            if (token != null) {
                val request = SendMessageRequest(resourceId, content, null)
                apiService.sendMessage(request, "Bearer $token")
            }
        } catch (e: Exception) {
            // Se falhar a rede, a mensagem continua no Room para posterior sincronização
            println("Erro ao enviar para API: ${e.message}")
        }
    }

    suspend fun markMessagesAsRead(resourceId: String) {
        messageDao.markAsRead(resourceId)
    }

    // ============ AVALIAÇÕES ============
    fun getUserRatings(userId: String): Flow<List<com.fiap.comunidadeconectada.data.entities.RatingEntity>> {
        return ratingDao.getRatingsForUser(userId)
    }

    suspend fun getUserAverageRating(userId: String): Float? {
        return ratingDao.getAverageRating(userId)
    }

    // ============ TRANSAÇÕES ============
    fun getUserTransactions(userId: String): Flow<List<com.fiap.comunidadeconectada.data.entities.TransactionEntity>> {
        return transactionDao.getTransactionsByRequester(userId)
    }

    fun getTransactionsByStatus(status: String): Flow<List<com.fiap.comunidadeconectada.data.entities.TransactionEntity>> {
        return transactionDao.getTransactionsByStatus(status)
    }

    // ============ EVENTOS ============
    fun getUpcomingEvents(): Flow<List<com.fiap.comunidadeconectada.data.entities.EventEntity>> {
        return eventDao.getUpcomingEvents(System.currentTimeMillis())
    }

    fun getAllEvents(): Flow<List<com.fiap.comunidadeconectada.data.entities.EventEntity>> {
        return eventDao.getAllEvents()
    }

    // ============ POSTS ============
    fun getAllPosts(): Flow<List<com.fiap.comunidadeconectada.data.entities.PostEntity>> {
        return postDao.getAllPosts()
    }

    fun getPostsByAuthor(authorId: String): Flow<List<com.fiap.comunidadeconectada.data.entities.PostEntity>> {
        return postDao.getPostsByAuthor(authorId)
    }
}
