package com.fiap.comunidadeconectada.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fiap.comunidadeconectada.api.Content
import com.fiap.comunidadeconectada.api.GeminiApiService
import com.fiap.comunidadeconectada.api.GeminiRequest
import com.fiap.comunidadeconectada.api.Part
import com.fiap.comunidadeconectada.api.RetrofitClient
import com.fiap.comunidadeconectada.ui.theme.PrimaryBlue
import com.fiap.comunidadeconectada.ui.theme.SecondaryGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ChatbotMessage(
    val id: String,
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotScreen(navController: NavHostController) {
    val messageText = remember { mutableStateOf("") }
    val messages = remember { mutableStateOf(listOf<ChatbotMessage>()) }
    val isLoading = remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val geminiService = remember {
        RetrofitClient.createService(GeminiApiService::class.java, "https://generativelanguage.googleapis.com/")
    }
    val apiKey = "AIzaSyDitfy_ln9tGYr5E8CPEiB3jZjA042319c"

    // Inicialização
    LaunchedEffect(Unit) {
        if (messages.value.isEmpty()) {
            messages.value = listOf(
                ChatbotMessage(
                    id = "0",
                    text = "Olá! Eu sou o ConectaIA. Posso ajudar você a encontrar recursos ou explicar como nossa comunidade funciona. O que deseja saber?",
                    isFromUser = false
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F5F9))
    ) {
        // Top Bar Estilizada (Semelhante à Home)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(PrimaryBlue.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.AutoAwesome, null, tint = PrimaryBlue)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("ConectaIA", fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                    Text("Assistente Inteligente", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                }
            }
        }

        // Chat
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
        ) {
            items(messages.value) { message ->
                ChatBubble(message)
            }
            if (isLoading.value) {
                item { TypingIndicator() }
            }
        }

        // Input Field
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .navigationBarsPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = messageText.value,
                    onValueChange = { messageText.value = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Pergunte algo...", fontSize = 14.sp) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF8FAFC),
                        unfocusedContainerColor = Color(0xFFF8FAFC),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.width(8.dp))
                FloatingActionButton(
                    onClick = {
                        if (messageText.value.isNotBlank() && !isLoading.value) {
                            val userQuery = messageText.value
                            messages.value = messages.value + ChatbotMessage(
                                id = System.currentTimeMillis().toString(),
                                text = userQuery,
                                isFromUser = true
                            )
                            messageText.value = ""
                            isLoading.value = true
                            
                            scope.launch {
                                try {
                                    val request = GeminiRequest(
                                        contents = listOf(Content(parts = listOf(Part(text = userQuery))))
                                    )
                                    val response = geminiService.generateContent(apiKey, request)
                                    val botText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                                        ?: "Não consegui processar isso agora. Pode repetir?"
                                    
                                    messages.value = messages.value + ChatbotMessage(
                                        id = System.currentTimeMillis().toString(),
                                        text = botText,
                                        isFromUser = false
                                    )
                                } catch (e: Exception) {
                                    // Tratamento amigável de erro (ex: 503)
                                    delay(1000)
                                    messages.value = messages.value + ChatbotMessage(
                                        id = System.currentTimeMillis().toString(),
                                        text = "Ops! Meus circuitos estão um pouco sobrecarregados (Erro 503). Pode tentar novamente em alguns segundos?",
                                        isFromUser = false
                                    )
                                } finally {
                                    isLoading.value = false
                                    listState.animateScrollToItem(messages.value.size - 1)
                                }
                            }
                        }
                    },
                    containerColor = PrimaryBlue,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.size(48.dp),
                    elevation = FloatingActionButtonDefaults.elevation(0.dp)
                ) {
                    Icon(Icons.Filled.Send, null, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatbotMessage) {
    val arrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start
    val bgColor = if (message.isFromUser) PrimaryBlue else Color.White
    val textColor = if (message.isFromUser) Color.White else Color(0xFF1E293B)
    val shape = if (message.isFromUser) {
        RoundedCornerShape(16.dp, 16.dp, 2.dp, 16.dp)
    } else {
        RoundedCornerShape(16.dp, 16.dp, 16.dp, 2.dp)
    }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = arrangement) {
        Box(
            modifier = Modifier
                .shadow(if (message.isFromUser) 0.dp else 1.dp, shape)
                .background(bgColor, shape)
                .padding(12.dp)
                .widthIn(max = 280.dp)
        ) {
            Text(text = message.text, color = textColor, fontSize = 15.sp)
        }
    }
}

@Composable
fun TypingIndicator() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = PrimaryBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text("ConectaIA está pensando...", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
        }
    }
}
