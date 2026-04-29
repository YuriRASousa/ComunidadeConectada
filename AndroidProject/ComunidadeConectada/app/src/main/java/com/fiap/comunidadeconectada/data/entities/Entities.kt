package com.fiap.comunidadeconectada.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

// Entidade: Usuário
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String,
    val address: String,
    val profileImageUrl: String? = null,
    val reputation: Float = 0f,
    val totalTransactions: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val isVerified: Boolean = false
)

// Entidade: Recurso
@Entity(tableName = "resources")
data class ResourceEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val condition: String,
    val offerantId: String,
    val imageUrl: String? = null,
    val availability: String,
    val type: String, // "emprestar", "vender", "trocar", "serviço"
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
    val latitude: Double? = null,
    val longitude: Double? = null
)

// Entidade: Mensagem
@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["senderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ResourceEntity::class,
            parentColumns = ["id"],
            childColumns = ["resourceId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("senderId"), Index("resourceId")]
)
data class MessageEntity(
    @PrimaryKey
    val id: String,
    val senderId: String,
    val resourceId: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val attachmentUrl: String? = null
)

// Entidade: Avaliação
@Entity(
    tableName = "ratings",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["ratedUserId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["raterUserId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("ratedUserId"), Index("raterUserId")]
)
data class RatingEntity(
    @PrimaryKey
    val id: String,
    val ratedUserId: String,
    val raterUserId: String,
    val rating: Float,
    val comment: String,
    val timestamp: Long = System.currentTimeMillis()
)

// Entidade: Transação
@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["requesterId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ResourceEntity::class,
            parentColumns = ["id"],
            childColumns = ["resourceId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("requesterId"), Index("resourceId")]
)
data class TransactionEntity(
    @PrimaryKey
    val id: String,
    val requesterId: String,
    val resourceId: String,
    val status: String, // "pendente", "confirmada", "concluída", "cancelada"
    val requestedDate: Long,
    val completedDate: Long? = null,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

// Entidade: Evento
@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val date: Long,
    val time: String,
    val location: String,
    val organizerId: String,
    val imageUrl: String? = null,
    val participantsCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

// Entidade: Post no Mural
@Entity(
    tableName = "posts",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["authorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("authorId")]
)
data class PostEntity(
    @PrimaryKey
    val id: String,
    val authorId: String,
    val title: String,
    val content: String,
    val imageUrl: String? = null,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
