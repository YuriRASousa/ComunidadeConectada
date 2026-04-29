package com.fiap.comunidadeconectada.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fiap.comunidadeconectada.ai.LLMManager
import com.fiap.comunidadeconectada.ui.theme.PrimaryBlue
import com.fiap.comunidadeconectada.ui.theme.SecondaryGreen
import com.fiap.comunidadeconectada.ui.viewmodel.ResourceViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferResourceScreen(navController: NavHostController, viewModel: ResourceViewModel) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()
    val llmManager = remember { LLMManager() }
    var isAiLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Oferecer Recurso", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (title.isNotBlank()) {
                        isSubmitting = true
                        viewModel.addResource(title, description, category)
                        navController.popBackStack()
                    }
                },
                containerColor = SecondaryGreen,
                contentColor = Color.White
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Default.Check, contentDescription = "Salvar")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título do Recurso") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Ex: Furadeira Bosch 600W") },
                shape = RoundedCornerShape(12.dp)
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Categoria") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Ex: Ferramentas") },
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        if (isAiLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            IconButton(onClick = {
                                if (description.length > 5) {
                                    isAiLoading = true
                                    scope.launch {
                                        val suggestions = llmManager.suggestCategories(description)
                                        if (suggestions.isNotEmpty()) {
                                            category = suggestions.first()
                                        }
                                        isAiLoading = false
                                    }
                                }
                            }) {
                                Icon(Icons.Default.AutoAwesome, "Sugerir com IA", tint = PrimaryBlue)
                            }
                        }
                    }
                )
            }

            Column {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descrição detalhada") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    shape = RoundedCornerShape(12.dp),
                    placeholder = { Text("Descreva o estado do item e como ele pode ajudar...") }
                )
                
                if (description.isNotBlank()) {
                    TextButton(
                        onClick = {
                            isAiLoading = true
                            scope.launch {
                                val improved = llmManager.improveDescription(description)
                                description = improved
                                isAiLoading = false
                            }
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(Icons.Default.AutoAwesome, null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Melhorar com IA", fontSize = 12.sp)
                    }
                }
            }
            
            Card(
                colors = CardDefaults.cardColors(containerColor = PrimaryBlue.copy(alpha = 0.05f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AutoAwesome, null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Dica: Use a varinha mágica para que nossa IA ajude a categorizar e descrever seu item!",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF475569)
                    )
                }
            }
        }
    }
}
