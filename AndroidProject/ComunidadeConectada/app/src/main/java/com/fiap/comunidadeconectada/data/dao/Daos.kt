package com.fiap.comunidadeconectada.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.fiap.comunidadeconectada.data.entities.UserEntity
import com.fiap.comunidadeconectada.data.entities.ResourceEntity
import com.fiap.comunidadeconectada.data.entities.MessageEntity
import com.fiap.comunidadeconectada.data.entities.RatingEntity
import com.fiap.comunidadeconectada.data.entities.TransactionEntity
import com.fiap.comunidadeconectada.data.entities.EventEntity
import com.fiap.comunidadeconectada.data.entities.PostEntity
import kotlinx.coroutines.flow.Flow

import androidx.room.OnConflictStrategy

// DAO para Usuários
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Update
    suspend fun update(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserByIdFlow(userId: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>
}

// DAO para Recursos
@Dao
interface ResourceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(resource: ResourceEntity)

    @Update
    suspend fun update(resource: ResourceEntity)

    @Delete
    suspend fun delete(resource: ResourceEntity)

    @Query("SELECT * FROM resources WHERE id = :resourceId")
    suspend fun getResourceById(resourceId: String): ResourceEntity?

    @Query("SELECT * FROM resources WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getAllActiveResources(): Flow<List<ResourceEntity>>

    @Query("SELECT * FROM resources WHERE isActive = 1")
    suspend fun getAllActiveResourcesList(): List<ResourceEntity>

    @Query("SELECT * FROM resources WHERE offerantId = :offerantId AND isActive = 1")
    fun getResourcesByOfferant(offerantId: String): Flow<List<ResourceEntity>>

    @Query("SELECT * FROM resources WHERE category = :category AND isActive = 1")
    fun getResourcesByCategory(category: String): Flow<List<ResourceEntity>>

    @Query("SELECT u.name FROM users u WHERE u.id = :userId")
    suspend fun getUserNameById(userId: String): String?
}

// DAO para Mensagens
@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: MessageEntity)

    @Update
    suspend fun update(message: MessageEntity)

    @Delete
    suspend fun delete(message: MessageEntity)

    @Query("SELECT * FROM messages WHERE id = :messageId")
    suspend fun getMessageById(messageId: String): MessageEntity?

    @Query("SELECT * FROM messages WHERE resourceId = :resourceId ORDER BY timestamp DESC")
    fun getMessagesByResource(resourceId: String): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE senderId = :senderId ORDER BY timestamp DESC")
    fun getMessagesBySender(senderId: String): Flow<List<MessageEntity>>

    @Query("UPDATE messages SET isRead = 1 WHERE resourceId = :resourceId")
    suspend fun markAsRead(resourceId: String)
}

// DAO para Avaliações
@Dao
interface RatingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rating: RatingEntity)

    @Update
    suspend fun update(rating: RatingEntity)

    @Delete
    suspend fun delete(rating: RatingEntity)

    @Query("SELECT * FROM ratings WHERE ratedUserId = :userId ORDER BY timestamp DESC")
    fun getRatingsForUser(userId: String): Flow<List<RatingEntity>>

    @Query("SELECT AVG(rating) FROM ratings WHERE ratedUserId = :userId")
    suspend fun getAverageRating(userId: String): Float?
}

// DAO para Transações
@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity)

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE id = :transactionId")
    suspend fun getTransactionById(transactionId: String): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE requesterId = :userId ORDER BY createdAt DESC")
    fun getTransactionsByRequester(userId: String): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE status = :status")
    fun getTransactionsByStatus(status: String): Flow<List<TransactionEntity>>
}

// DAO para Eventos
@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: EventEntity)

    @Update
    suspend fun update(event: EventEntity)

    @Delete
    suspend fun delete(event: EventEntity)

    @Query("SELECT * FROM events WHERE id = :eventId")
    suspend fun getEventById(eventId: String): EventEntity?

    @Query("SELECT * FROM events WHERE date >= :currentDate ORDER BY date ASC")
    fun getUpcomingEvents(currentDate: Long): Flow<List<EventEntity>>

    @Query("SELECT * FROM events ORDER BY date DESC")
    fun getAllEvents(): Flow<List<EventEntity>>
}

// DAO para Posts
@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity)

    @Update
    suspend fun update(post: PostEntity)

    @Delete
    suspend fun delete(post: PostEntity)

    @Query("SELECT * FROM posts WHERE id = :postId")
    suspend fun getPostById(postId: String): PostEntity?

    @Query("SELECT * FROM posts ORDER BY createdAt DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE authorId = :authorId ORDER BY createdAt DESC")
    fun getPostsByAuthor(authorId: String): Flow<List<PostEntity>>
}
