package com.fiap.comunidadeconectada.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.fiap.comunidadeconectada.data.Repository
import com.fiap.comunidadeconectada.data.entities.ResourceEntity
import com.fiap.comunidadeconectada.navigation.Screen
import com.fiap.comunidadeconectada.ui.components.PrimaryButton
import com.fiap.comunidadeconectada.ui.theme.PrimaryBlue
import com.fiap.comunidadeconectada.ui.theme.ReputationExcellent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceDetailScreen(navController: NavHostController, resourceId: String, repository: Repository) {
    var resource by remember { mutableStateOf<ResourceEntity?>(null) }
    var ownerName by remember { mutableStateOf("Carregando...") }

    LaunchedEffect(resourceId) {
        val res = repository.getResourceById(resourceId)
        resource = res
        if (res != null) {
            ownerName = repository.getUserNameById(res.offerantId) ?: "Membro da Comunidade"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        val currentResource = resource
        if (currentResource == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryBlue)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Imagem Real do Recurso
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(Color(0xFFE5E7EB))
                ) {
                    if (!currentResource.imageUrl.isNullOrEmpty()) {
                        AsyncImage(
                            model = currentResource.imageUrl,
                            contentDescription = currentResource.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Imagem não disponível", color = Color.Gray)
                        }
                    }
                }

                Column(modifier = Modifier.padding(20.dp)) {
                    // Categoria Badge
                    Surface(
                        color = PrimaryBlue.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = currentResource.category.uppercase(),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Título
                    Text(
                        text = currentResource.title,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(bottom = 8.dp),
                        color = Color(0xFF1E293B)
                    )

                    // Reputação e Avaliações
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 24.dp)
                    ) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = if (index < 4) ReputationExcellent else Color.LightGray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = " 4.5 (8 avaliações)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    SectionTitle("Descrição")
                    Text(
                        text = currentResource.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF475569),
                        modifier = Modifier.padding(bottom = 20.dp),
                        lineHeight = 24.sp
                    )

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            SectionTitle("Condição")
                            Text(currentResource.condition, color = Color(0xFF1E293B), fontWeight = FontWeight.SemiBold)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            SectionTitle("Tipo")
                            Text(currentResource.type, color = PrimaryBlue, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    SectionTitle("Disponibilidade")
                    Text(
                        text = currentResource.availability,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF475569),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // Perfil do Ofertante (VINCULADO)
                    SectionTitle("Ofertante")
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFF8FAFC),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(PrimaryBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    ownerName.take(1),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = ownerName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Membro verificado • Proximidade: 1.2km",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Botão de Ação que abre o chat vinculado
                    PrimaryButton(
                        text = "SOLICITAR AGORA",
                        onClick = {
                            navController.navigate(Screen.Chat.createRoute(currentResource.id))
                        }
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}
