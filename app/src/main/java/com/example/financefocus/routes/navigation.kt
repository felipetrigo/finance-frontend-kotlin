package com.example.financefocus.routes

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.financefocus.Conta
import com.example.financefocus.Routes
import com.example.financefocus.dto.SharedViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavigation(navController: NavHostController) {
    val sharedViewModel: SharedViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.START
    ) {
        composable(Routes.START) {
            StartScreen(navController)
        }
        composable(Routes.LOGIN) {
            LoginScreen(navController,sharedViewModel)
        }
        composable(Routes.CADASTRO) {
            CadastroScreen(navController,sharedViewModel)
        }
        composable(Routes.GASTOS) {
            GastosScreen(navController,sharedViewModel)
        }
        composable(Routes.CONTA) {
            Conta(navController,sharedViewModel)
        }
        composable(Routes.LEARNING) {
            LearningScreen(navController = navController)
        }
        composable(Routes.ADDSPENT) {
            AddSpentScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        composable(Routes.EDITSPENT) {
            EditSpentScreen(navController = navController, sharedViewModel = sharedViewModel)
        }

        composable(
            route = Routes.VIDEO,
            arguments = listOf(
                navArgument("uri") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType },
                navArgument("author") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val uri = URLDecoder.decode(backStackEntry.arguments?.getString("uri"), StandardCharsets.UTF_8.name())
            val title = URLDecoder.decode(backStackEntry.arguments?.getString("title"), StandardCharsets.UTF_8.name())
            val author = URLDecoder.decode(backStackEntry.arguments?.getString("author"), StandardCharsets.UTF_8.name())
            VideoScreen(
                navController = navController,
                videoUri = uri,
                videoTitle = title,
                videoAuthor = author
            )
        }
    }
}