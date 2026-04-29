package com.fiap.comunidadeconectada.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.fiap.comunidadeconectada.ui.screens.*
import com.fiap.comunidadeconectada.ui.viewmodel.ChatViewModel
import com.fiap.comunidadeconectada.ui.viewmodel.ChatViewModelFactory
import com.fiap.comunidadeconectada.ui.viewmodel.ResourceViewModel
import com.fiap.comunidadeconectada.data.Repository

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Chat : Screen("chat/{chatId}") {
        fun createRoute(chatId: String) = "chat/$chatId"
    }
    object ResourceDetail : Screen("resource_detail/{resourceId}") {
        fun createRoute(resourceId: String) = "resource_detail/$resourceId"
    }
    object OfferResource : Screen("offer_resource")
    object Events : Screen("events")
    object Chatbot : Screen("chatbot")
    object ChatList : Screen("chat_list")
}

@Composable
fun ComunidadeConectadaNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Onboarding.route,
    resourceViewModel: ResourceViewModel,
    repository: Repository
) {
    val chatViewModel: ChatViewModel = viewModel(
        factory = ChatViewModelFactory(repository)
    )

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(navController)
        }

        composable(Screen.Home.route) {
            HomeScreen(navController, resourceViewModel)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

        composable(
            route = Screen.Chat.route,
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            ChatScreen(navController, chatId, chatViewModel)
        }

        composable(
            route = Screen.ResourceDetail.route,
            arguments = listOf(navArgument("resourceId") { type = NavType.StringType })
        ) { backStackEntry ->
            val resourceId = backStackEntry.arguments?.getString("resourceId") ?: ""
            ResourceDetailScreen(navController, resourceId, repository)
        }

        composable(Screen.OfferResource.route) {
            OfferResourceScreen(navController, resourceViewModel)
        }

        composable(Screen.Chatbot.route) {
            ChatbotScreen(navController)
        }

        composable(Screen.ChatList.route) {
            ChatListScreen(navController, chatViewModel)
        }
        
        // Se houver uma EventsScreen futura, ela entraria aqui
        // composable(Screen.Events.route) { EventsScreen(navController) }
    }
}
