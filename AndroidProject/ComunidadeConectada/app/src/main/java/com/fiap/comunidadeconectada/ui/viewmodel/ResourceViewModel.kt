package com.fiap.comunidadeconectada.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fiap.comunidadeconectada.data.Repository
import com.fiap.comunidadeconectada.data.entities.ResourceEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Modelo de UI para manter a compatibilidade com os componentes existentes
data class ResourceUiModel(
    val id: String,
    val title: String,
    val description: String,
    val reputation: Float,
    val offerantName: String,
    val category: String,
    val imageUrl: String? = null
)

class ResourceViewModel(private val repository: Repository) : ViewModel() {

    // Observa o banco de dados e mapeia para o modelo de UI
    val resources: StateFlow<List<ResourceUiModel>> = repository.getAllResources()
        .map { entities ->
            entities.map { entity ->
                val ownerName = repository.getUserNameById(entity.offerantId) ?: "Usuário"
                ResourceUiModel(
                    id = entity.id,
                    title = entity.title,
                    description = entity.description,
                    reputation = 4.8f,
                    offerantName = ownerName,
                    category = entity.category,
                    imageUrl = entity.imageUrl
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addResource(title: String, description: String, category: String) {
        viewModelScope.launch {
            repository.createResource(title, description, category)
        }
    }
}

class ResourceViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResourceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ResourceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
