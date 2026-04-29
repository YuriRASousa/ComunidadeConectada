package com.fiap.comunidadeconectada.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fiap.comunidadeconectada.navigation.ComunidadeConectadaNavGraph
import com.fiap.comunidadeconectada.navigation.Screen
import com.fiap.comunidadeconectada.ui.theme.PrimaryBlue
import com.fiap.comunidadeconectada.ui.theme.PrimaryBlueDark
import com.fiap.comunidadeconectada.ui.theme.SecondaryGreen
import com.fiap.comunidadeconectada.data.Repository
import com.fiap.comunidadeconectada.ui.viewmodel.ResourceViewModel

@Composable
fun MainScreen(resourceViewModel: ResourceViewModel, repository: Repository) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Telas onde a barra inferior NÃO deve aparecer
    val hideBottomBarList = listOf(Screen.Onboarding.route)
    val shouldShowBottomBar = currentDestination?.route !in hideBottomBarList

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavigationBar(navController)
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            ComunidadeConectadaNavGraph(
                navController = navController,
                resourceViewModel = resourceViewModel,
                repository = repository
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF0F172A), // Azul Noite Moderno
        shadowElevation = 24.dp
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            modifier = Modifier.height(80.dp)
        ) {
            // Home
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == Screen.Home.route } == true,
                onClick = { navController.navigate(Screen.Home.route) },
                icon = { Icon(Icons.Filled.Home, "Início") },
                label = { Text("Home") },
                colors = navigationBarItemColors()
            )

            // IA
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == Screen.Chatbot.route } == true,
                onClick = { navController.navigate(Screen.Chatbot.route) },
                icon = { Icon(Icons.Filled.AutoAwesome, "IA") },
                label = { Text("ConectaIA") },
                colors = navigationBarItemColors()
            )

            // Central Adicionar (Botão Especial)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.OfferResource.route) },
                    containerColor = SecondaryGreen,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.size(54.dp),
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp)
                ) {
                    Icon(Icons.Filled.Add, "Oferecer", modifier = Modifier.size(32.dp))
                }
            }

            // Chats
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == Screen.ChatList.route } == true,
                onClick = { navController.navigate(Screen.ChatList.route) },
                icon = { Icon(Icons.Filled.QuestionAnswer, "Mensagens") },
                label = { Text("Chats") },
                colors = navigationBarItemColors()
            )

            // Perfil
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == Screen.Profile.route } == true,
                onClick = { navController.navigate(Screen.Profile.route) },
                icon = { Icon(Icons.Filled.AccountCircle, "Perfil") },
                label = { Text("Conta") },
                colors = navigationBarItemColors()
            )
        }
    }
}

@Composable
fun navigationBarItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = Color.White,
    selectedTextColor = Color.White,
    unselectedIconColor = Color.Gray,
    unselectedTextColor = Color.Gray,
    indicatorColor = PrimaryBlue.copy(alpha = 0.3f)
)

@Composable
fun AppLogo() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    brush = Brush.linearGradient(listOf(SecondaryGreen, PrimaryBlue)),
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.AllInclusive, null, tint = Color.White, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                "COMUNIDADE",
                style = MaterialTheme.typography.labelSmall,
                color = PrimaryBlue,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.5.sp
            )
            Text(
                "CONECTADA",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF1E293B),
                fontWeight = FontWeight.Black,
                lineHeight = 16.sp
            )
        }
    }
}
