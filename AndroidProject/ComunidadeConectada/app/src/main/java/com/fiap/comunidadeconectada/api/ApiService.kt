package com.fiap.comunidadeconectada.api

import com.fiap.comunidadeconectada.api.models.ApiResponse
import com.fiap.comunidadeconectada.api.models.CreateRatingRequest
import com.fiap.comunidadeconectada.api.models.CreateResourceRequest
import com.fiap.comunidadeconectada.api.models.EventResponse
import com.fiap.comunidadeconectada.api.models.LoginRequest
import com.fiap.comunidadeconectada.api.models.LoginResponse
import com.fiap.comunidadeconectada.api.models.MessageResponse
import com.fiap.comunidadeconectada.api.models.PostResponse
import com.fiap.comunidadeconectada.api.models.RatingResponse
import com.fiap.comunidadeconectada.api.models.RegisterRequest
import com.fiap.comunidadeconectada.api.models.ResourceResponse
import com.fiap.comunidadeconectada.api.models.SendMessageRequest
import com.fiap.comunidadeconectada.api.models.TransactionResponse
import com.fiap.comunidadeconectada.api.models.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {

    // ============ AUTENTICAÇÃO ============
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): ApiResponse<UserResponse>

    // ============ USUÁRIOS ============
    @GET("users/{userId}")
    suspend fun getUser(
        @Path("userId") userId: String,
        @Header("Authorization") token: String
    ): ApiResponse<UserResponse>

    @GET("users")
    suspend fun getAllUsers(
        @Header("Authorization") token: String
    ): ApiResponse<List<UserResponse>>

    @PUT("users/{userId}")
    suspend fun updateUser(
        @Path("userId") userId: String,
        @Body user: UserResponse,
        @Header("Authorization") token: String
    ): ApiResponse<UserResponse>

    // ============ RECURSOS ============
    @GET("resources")
    suspend fun getAllResources(
        @Header("Authorization") token: String,
        @Query("category") category: String? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): ApiResponse<List<ResourceResponse>>

    @GET("resources/{resourceId}")
    suspend fun getResource(
        @Path("resourceId") resourceId: String,
        @Header("Authorization") token: String
    ): ApiResponse<ResourceResponse>

    @POST("resources")
    suspend fun createResource(
        @Body request: CreateResourceRequest,
        @Header("Authorization") token: String
    ): ApiResponse<ResourceResponse>

    @GET("resources/user/{userId}")
    suspend fun getUserResources(
        @Path("userId") userId: String,
        @Header("Authorization") token: String
    ): ApiResponse<List<ResourceResponse>>

    @GET("resources/category/{category}")
    suspend fun getResourcesByCategory(
        @Path("category") category: String,
        @Header("Authorization") token: String
    ): ApiResponse<List<ResourceResponse>>

    // ============ MENSAGENS ============
    @GET("messages/resource/{resourceId}")
    suspend fun getResourceMessages(
        @Path("resourceId") resourceId: String,
        @Header("Authorization") token: String
    ): ApiResponse<List<MessageResponse>>

    @POST("messages")
    suspend fun sendMessage(
        @Body request: SendMessageRequest,
        @Header("Authorization") token: String
    ): ApiResponse<MessageResponse>

    @PUT("messages/{messageId}/read")
    suspend fun markMessageAsRead(
        @Path("messageId") messageId: String,
        @Header("Authorization") token: String
    ): ApiResponse<MessageResponse>

    // ============ AVALIAÇÕES ============
    @GET("ratings/user/{userId}")
    suspend fun getUserRatings(
        @Path("userId") userId: String,
        @Header("Authorization") token: String
    ): ApiResponse<List<RatingResponse>>

    @POST("ratings")
    suspend fun createRating(
        @Body request: CreateRatingRequest,
        @Header("Authorization") token: String
    ): ApiResponse<RatingResponse>

    // ============ TRANSAÇÕES ============
    @GET("transactions/user/{userId}")
    suspend fun getUserTransactions(
        @Path("userId") userId: String,
        @Header("Authorization") token: String
    ): ApiResponse<List<TransactionResponse>>

    @GET("transactions/{transactionId}")
    suspend fun getTransaction(
        @Path("transactionId") transactionId: String,
        @Header("Authorization") token: String
    ): ApiResponse<TransactionResponse>

    @POST("transactions")
    suspend fun createTransaction(
        @Body transaction: TransactionResponse,
        @Header("Authorization") token: String
    ): ApiResponse<TransactionResponse>

    @PUT("transactions/{transactionId}")
    suspend fun updateTransaction(
        @Path("transactionId") transactionId: String,
        @Body transaction: TransactionResponse,
        @Header("Authorization") token: String
    ): ApiResponse<TransactionResponse>

    // ============ EVENTOS ============
    @GET("events")
    suspend fun getAllEvents(
        @Header("Authorization") token: String
    ): ApiResponse<List<EventResponse>>

    @GET("events/{eventId}")
    suspend fun getEvent(
        @Path("eventId") eventId: String,
        @Header("Authorization") token: String
    ): ApiResponse<EventResponse>

    @POST("events")
    suspend fun createEvent(
        @Body event: EventResponse,
        @Header("Authorization") token: String
    ): ApiResponse<EventResponse>

    @POST("events/{eventId}/participate")
    suspend fun participateInEvent(
        @Path("eventId") eventId: String,
        @Header("Authorization") token: String
    ): ApiResponse<EventResponse>

    // ============ POSTS ============
    @GET("posts")
    suspend fun getAllPosts(
        @Header("Authorization") token: String
    ): ApiResponse<List<PostResponse>>

    @GET("posts/{postId}")
    suspend fun getPost(
        @Path("postId") postId: String,
        @Header("Authorization") token: String
    ): ApiResponse<PostResponse>

    @POST("posts")
    suspend fun createPost(
        @Body post: PostResponse,
        @Header("Authorization") token: String
    ): ApiResponse<PostResponse>

    @PUT("posts/{postId}")
    suspend fun updatePost(
        @Path("postId") postId: String,
        @Body post: PostResponse,
        @Header("Authorization") token: String
    ): ApiResponse<PostResponse>
}
