package com.fiap.comunidadeconectada.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.fiap.comunidadeconectada.data.entities.MessageEntity
import com.fiap.comunidadeconectada.ui.theme.PrimaryBlue
import com.fiap.comunidadeconectada.ui.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavHostController,
    resourceId: String,
    viewModel: ChatViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val messageText = remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(resourceId) {
        viewModel.loadChat(resourceId)
    }

    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F5F9))
    ) {
        // Top Bar Customizada
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar", tint = Color(0xFF1E293B))
                }
                
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(PrimaryBlue.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (uiState.resource?.title?.firstOrNull() ?: "?").toString(),
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column {
                    Text(
                        text = uiState.resource?.title ?: "Carregando...",
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "Conversando sobre o anúncio",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }
        }

        if (uiState.isLoading) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryBlue)
            }
        } else {
            // Área de mensagens
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp)
            ) {
                items(uiState.messages) { message ->
                    UserChatBubble(message, uiState.currentUser?.id ?: "")
                }
            }
        }

        // Input Field
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding(),
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
                    placeholder = { Text("Escreva uma mensagem...", fontSize = 14.sp) },
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
                        if (messageText.value.isNotBlank()) {
                            viewModel.sendMessage(resourceId, messageText.value)
                            messageText.value = ""
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
fun UserChatBubble(message: MessageEntity, currentUserId: String) {
    val isFromCurrentUser = message.senderId == currentUserId
    val arrangement = if (isFromCurrentUser) Arrangement.End else Arrangement.Start
    val bgColor = if (isFromCurrentUser) PrimaryBlue else Color.White
    val textColor = if (isFromCurrentUser) Color.White else Color(0xFF1E293B)
    val shape = if (isFromCurrentUser) {
        RoundedCornerShape(16.dp, 16.dp, 2.dp, 16.dp)
    } else {
        RoundedCornerShape(16.dp, 16.dp, 16.dp, 2.dp)
    }

    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeString = sdf.format(Date(message.timestamp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = arrangement) {
        Box(
            modifier = Modifier
                .shadow(if (isFromCurrentUser) 0.dp else 1.dp, shape)
                .background(bgColor, shape)
                .padding(12.dp)
                .widthIn(max = 280.dp)
        ) {
            Column {
                Text(text = message.content, color = textColor, fontSize = 15.sp)
                Text(
                    text = timeString,
                    fontSize = 10.sp,
                    color = if (isFromCurrentUser) Color.White.copy(alpha = 0.7f) else Color.Gray,
                    modifier = Modifier.align(Alignment.End).padding(top = 4.dp)
                )
            }
        }
    }
}
