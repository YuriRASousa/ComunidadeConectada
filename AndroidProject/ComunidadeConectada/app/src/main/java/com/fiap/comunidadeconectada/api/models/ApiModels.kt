package com.fiap.comunidadeconectada.api.models

import com.google.gson.annotations.SerializedName

// Resposta genérica da API
data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: T?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("error")
    val error: String?
)

// Modelo de Usuário
data class UserResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("profileImageUrl")
    val profileImageUrl: String?,
    @SerializedName("reputation")
    val reputation: Float,
    @SerializedName("totalTransactions")
    val totalTransactions: Int,
    @SerializedName("isVerified")
    val isVerified: Boolean,
    @SerializedName("createdAt")
    val createdAt: Long
)

// Modelo de Recurso
data class ResourceResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("condition")
    val condition: String,
    @SerializedName("offerant")
    val offerant: UserResponse,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("availability")
    val availability: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("createdAt")
    val createdAt: Long
)

// Modelo de Mensagem
data class MessageResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("sender")
    val sender: UserResponse,
    @SerializedName("resourceId")
    val resourceId: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("timestamp")
    val timestamp: Long,
    @SerializedName("isRead")
    val isRead: Boolean,
    @SerializedName("attachmentUrl")
    val attachmentUrl: String?
)

// Modelo de Avaliação
data class RatingResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("ratedUser")
    val ratedUser: UserResponse,
    @SerializedName("rater")
    val rater: UserResponse,
    @SerializedName("rating")
    val rating: Float,
    @SerializedName("comment")
    val comment: String,
    @SerializedName("timestamp")
    val timestamp: Long
)

// Modelo de Transação
data class TransactionResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("requester")
    val requester: UserResponse,
    @SerializedName("resource")
    val resource: ResourceResponse,
    @SerializedName("status")
    val status: String,
    @SerializedName("requestedDate")
    val requestedDate: Long,
    @SerializedName("completedDate")
    val completedDate: Long?,
    @SerializedName("notes")
    val notes: String?,
    @SerializedName("createdAt")
    val createdAt: Long
)

// Modelo de Evento
data class EventResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("date")
    val date: Long,
    @SerializedName("time")
    val time: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("organizer")
    val organizer: UserResponse,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("participantsCount")
    val participantsCount: Int,
    @SerializedName("createdAt")
    val createdAt: Long
)

// Modelo de Post
data class PostResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("author")
    val author: UserResponse,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("likesCount")
    val likesCount: Int,
    @SerializedName("commentsCount")
    val commentsCount: Int,
    @SerializedName("createdAt")
    val createdAt: Long
)

// Modelo de Login
data class LoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

data class LoginResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("user")
    val user: UserResponse
)

// Modelo de Registro
data class RegisterRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("address")
    val address: String
)

// Modelo de Criação de Recurso
data class CreateResourceRequest(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("condition")
    val condition: String,
    @SerializedName("availability")
    val availability: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?
)

// Modelo de Envio de Mensagem
data class SendMessageRequest(
    @SerializedName("resourceId")
    val resourceId: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("attachmentUrl")
    val attachmentUrl: String?
)

// Modelo de Avaliação
data class CreateRatingRequest(
    @SerializedName("ratedUserId")
    val ratedUserId: String,
    @SerializedName("rating")
    val rating: Float,
    @SerializedName("comment")
    val comment: String
)
