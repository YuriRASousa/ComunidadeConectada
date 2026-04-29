package com.fiap.comunidadeconectada.api

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

// Modelos de dados para a API do Gemini
data class GeminiRequest(
    val contents: List<Content>
)

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String
)

data class GeminiResponse(
    val candidates: List<Candidate>?
)

data class Candidate(
    val content: Content?
)

interface GeminiApiService {
    // Rota EXATA extraída do seu Get Code (Gemini 3 Flash Preview)
    @POST("v1beta/models/gemini-3-flash-preview:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}
