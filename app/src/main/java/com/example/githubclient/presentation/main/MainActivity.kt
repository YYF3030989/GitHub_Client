package com.example.githubclient.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.githubclient.presentation.screen.AuthScreen
import com.example.githubclient.presentation.screen.GithubUserDetailScreen
import com.example.githubclient.presentation.screen.GithubUserListScreen
import com.example.githubclient.presentation.viewmodel.AuthViewModel
import com.example.githubclient.ui.theme.GithubClientTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val token by authViewModel.accessToken.collectAsState()
            val isGuest by authViewModel.isGuest.collectAsState()

            LaunchedEffect(token, isGuest) {
                val isLoggedOut = token.isNullOrEmpty() && !isGuest
                if (isLoggedOut) {
                    navController.navigate("auth") {
                        popUpTo("users") { inclusive = true }
                    }
                }
            }

            GithubClientTheme {
                NavHost(
                    modifier = Modifier.Companion.fillMaxSize(),
                    navController = navController,
                    startDestination = if (token.isNullOrEmpty() && !isGuest) "auth" else "users"
                ) {
                    composable("auth") {
                        AuthScreen {
                            navController.navigate("users") {
                                popUpTo("auth") { inclusive = true }
                            }
                        }
                    }
                    composable("users") {
                        GithubUserListScreen(
                            navController = navController,
                            onLogout = {
                                Log.d("MainActivity", "Logging out")
                                authViewModel.logout(this@MainActivity)
                            }
                        )
                    }
                    composable(
                        route = "user/{userId}/{login}",
                        arguments = listOf(navArgument("login") {
                            type = NavType.Companion.StringType
                        })
                    ) { backStackEntry ->
                        val login = backStackEntry.arguments?.getString("login")
                        GithubUserDetailScreen(
                            username = login.toString(),
                            onBackClick = {
                                navController.popBackStack()
                            })
                    }
                }
            }
        }
    }
}