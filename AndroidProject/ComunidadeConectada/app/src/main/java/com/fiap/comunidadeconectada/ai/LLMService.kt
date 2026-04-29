package com.fiap.comunidadeconectada.ai

import com.fiap.comunidadeconectada.api.Content
import com.fiap.comunidadeconectada.api.GeminiApiService
import com.fiap.comunidadeconectada.api.GeminiRequest
import com.fiap.comunidadeconectada.api.Part
import com.fiap.comunidadeconectada.api.RetrofitClient

/**
 * Classe para gerenciar interações com a IA do Google Gemini
 * Implementa as funcionalidades de categorização, melhoria de texto e segurança.
 */
class LLMManager {

    private val geminiService = RetrofitClient.createService(
        GeminiApiService::class.java, 
        "https://generativelanguage.googleapis.com/"
    )
    
    // Chave de API extraída do Chatbot funcional
    private val apiKey = "AIzaSyDitfy_ln9tGYr5E8CPEiB3jZjA042319c"

    /**
     * Gera uma resposta genérica usando o Gemini
     */
    private suspend fun callGemini(prompt: String): String {
        return try {
            val request = GeminiRequest(
                contents = listOf(Content(parts = listOf(Part(text = prompt))))
            )
            val response = geminiService.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * Gera sugestões de categorias para um recurso baseado em descrição
     */
    suspend fun suggestCategories(description: String): List<String> {
        val prompt = """
            Você é um assistente da plataforma Comunidade Conectada.
            Com base na descrição do item abaixo, sugira de 1 a 3 categorias curtas (máximo 2 palavras cada).
            Retorne apenas as categorias separadas por vírgula, sem explicações.
            Item: $description
        """.trimIndent()

        val response = callGemini(prompt)
        return if (response.isNotEmpty()) {
            response.split(",").map { it.trim() }.filter { it.isNotBlank() }
        } else {
            listOf("Outros")
        }
    }

    /**
     * Valida e melhora a descrição de um recurso
     */
    suspend fun improveDescription(description: String): String {
        val prompt = """
            Melhore a descrição abaixo para torná-la mais clara e atrativa para uma comunidade de compartilhamento.
            Mantenha o texto curto (máximo 2 parágrafos).
            Texto original: $description
        """.trimIndent()

        val response = callGemini(prompt)
        return response.ifEmpty { description }
    }

    /**
     * Detecta possíveis problemas de segurança em uma mensagem
     */
    suspend fun checkSafety(message: String): SafetyResult {
        val prompt = """
            Analise se a mensagem abaixo é segura para uma comunidade (sem ofensas, golpes ou conteúdo impróprio).
            Responda EXATAMENTE no formato: "SAFE: motivo" ou "UNSAFE: motivo".
            Mensagem: $message
        """.trimIndent()

        val response = callGemini(prompt)
        return if (response.startsWith("UNSAFE", ignoreCase = true)) {
            SafetyResult(isSafe = false, reason = response.substringAfter(":").trim())
        } else {
            SafetyResult(isSafe = true, reason = "Mensagem segura")
        }
    }
}

data class SafetyResult(
    val isSafe: Boolean,
    val reason: String
)
