package com.fiap.comunidadeconectada

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fiap.comunidadeconectada.ui.screens.MainScreen
import com.fiap.comunidadeconectada.ui.theme.ComunidadeConectadaTheme
import com.fiap.comunidadeconectada.ui.viewmodel.ResourceViewModel
import com.fiap.comunidadeconectada.ui.viewmodel.ResourceViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val repository = (application as ComunidadeConectadaApplication).repository
        
        setContent {
            ComunidadeConectadaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Injeção manual do repositório no ViewModel
                    val resourceViewModel: ResourceViewModel = viewModel(
                        factory = ResourceViewModelFactory(repository)
                    )
                    
                    // MainScreen agora gerencia o NavGraph e a BottomBar persistente
                    MainScreen(
                        resourceViewModel = resourceViewModel,
                        repository = repository
                    )
                }
            }
        }
    }
}
