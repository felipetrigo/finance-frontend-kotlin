package com.example.financefocus.routes

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.financefocus.Conta
import com.example.financefocus.Routes
import com.example.financefocus.dto.SharedViewModel

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
  }
}