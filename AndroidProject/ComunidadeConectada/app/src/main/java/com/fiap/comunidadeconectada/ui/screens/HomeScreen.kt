package com.fiap.comunidadeconectada.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fiap.comunidadeconectada.navigation.Screen
import com.fiap.comunidadeconectada.ui.components.ResourceCard
import com.fiap.comunidadeconectada.ui.viewmodel.ResourceViewModel

@Composable
fun HomeScreen(navController: NavHostController, viewModel: ResourceViewModel) {
    val resourcesList by viewModel.resources.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F5F9)) // Slate 100 - Fundo mais agradável que branco puro
    ) {
        // Top Bar Customizada com o Novo Logo
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                AppLogo() // Usando o componente de logo centralizado
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp, top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Recursos Próximos",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A)
                )
            }
            
            items(resourcesList) { resource ->
                val imageUrl = if (!resource.imageUrl.isNullOrEmpty()) {
                    resource.imageUrl
                } else {
                    val titleLower = resource.title.lowercase()
                    when {
                        titleLower.contains("furadeira") || titleLower.contains("ferramenta") -> 
                            "https://images.unsplash.com/photo-1504148455328-c376907d081c?w=500"
                        titleLower.contains("alimento") || titleLower.contains("cesta") -> 
                            "https://images.unsplash.com/photo-1542838132-92c53300491e?w=500"
                        titleLower.contains("aula") || titleLower.contains("professor") || titleLower.contains("ensino") -> 
                            "https://images.unsplash.com/photo-1524178232363-1fb2b075b655?w=500"
                        titleLower.contains("escada") -> 
                            "https://images.unsplash.com/photo-1595429111812-329406087968?w=500"
                        titleLower.contains("cadeira de rodas") || titleLower.contains("saúde") || titleLower.contains("hospitalar") ->
                            "https://images.unsplash.com/photo-1576765608535-5f04d1e3f289?w=500"
                        titleLower.contains("fone") || titleLower.contains("headset") || titleLower.contains("ouvido") || titleLower.contains("bluetooth") ->
                            "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500"
                        else -> "https://images.unsplash.com/photo-1589939705384-5185137a7f0f?w=500"
                    }
                }

                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    ResourceCard(
                        title = resource.title,
                        description = resource.description,
                        reputation = resource.reputation,
                        offerantName = resource.offerantName,
                        imageUrl = imageUrl,
                        onCardClick = {
                            navController.navigate(Screen.ResourceDetail.createRoute(resource.id))
                        },
                        onRequestClick = {
                            navController.navigate(Screen.Chat.createRoute(resource.id))
                        }
                    )
                }
            }
        }
    }
}
