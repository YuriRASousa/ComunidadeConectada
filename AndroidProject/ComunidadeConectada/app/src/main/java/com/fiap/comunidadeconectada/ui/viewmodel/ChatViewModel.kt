package com.fiap.comunidadeconectada.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fiap.comunidadeconectada.data.Repository
import com.fiap.comunidadeconectada.data.entities.MessageEntity
import com.fiap.comunidadeconectada.data.entities.ResourceEntity
import com.fiap.comunidadeconectada.data.entities.UserEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ChatUiState(
    val messages: List<MessageEntity> = emptyList(),
    val resource: ResourceEntity? = null,
    val currentUser: UserEntity? = null,
    val isLoading: Boolean = false
)

data class ChatSummaryUiModel(
    val resourceId: String,
    val otherUserName: String,
    val resourceTitle: String,
    val lastMessage: String,
    val timestamp: Long,
    val otherUserId: String
)

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModel(private val repository: Repository) : ViewModel() {

    private val _resourceIdFlow = MutableSharedFlow<String>(replay = 1)

    val uiState: StateFlow<ChatUiState> = _resourceIdFlow
        .flatMapLatest { resId ->
            combine(
                repository.getResourceMessages(resId),
                repository.getCurrentUser(),
                flow { emit(repository.getResourceById(resId)) }
            ) { messages, user, resource ->
                ChatUiState(
                    messages = messages,
                    currentUser = user,
                    resource = resource,
                    isLoading = false
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ChatUiState(isLoading = true))

    val chatList: StateFlow<List<ChatSummaryUiModel>> = repository.getCurrentUser()
        .flatMapLatest { user ->
            repository.getAllResources().map { resources ->
                resources.map { res ->
                    // Busca o nome real de quem postou o recurso
                    val ownerName = repository.getUserNameById(res.offerantId) ?: "Membro"
                    
                    ChatSummaryUiModel(
                        resourceId = res.id,
                        otherUserName = if (res.offerantId == user?.id) "Interessado" else ownerName,
                        resourceTitle = res.title,
                        lastMessage = "Toque para ver a conversa",
                        timestamp = res.createdAt,
                        otherUserId = res.offerantId
                    )
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun loadChat(resourceId: String) {
        viewModelScope.launch {
            _resourceIdFlow.emit(resourceId)
        }
    }

    fun sendMessage(resourceId: String, content: String) {
        if (content.isBlank()) return
        viewModelScope.launch {
            try {
                repository.sendMessage(resourceId, content)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class ChatViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
