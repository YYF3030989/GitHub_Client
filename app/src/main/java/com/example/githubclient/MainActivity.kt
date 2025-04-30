package com.example.githubclient

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.githubclient.ui.screen.AuthScreen
import com.example.githubclient.ui.screen.GithubUserDetailScreen
import com.example.githubclient.ui.screen.GithubUserListScreen
import com.example.githubclient.ui.theme.GithubClientTheme
import com.example.githubclient.ui.viewmodel.AuthViewModel
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

            GithubClientTheme {
                NavHost(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    startDestination = if (token.isNullOrEmpty()) "auth" else "users"
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
                            navController = navController
                        )
                    }
                    composable(
                        route = "user/{userId}/{login}",
                        arguments = listOf(navArgument("login") { type = NavType.StringType })
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

    override fun onNewIntent(intent: Intent) {
        Log.d("MainActivity", "onNewIntent called")
        super.onNewIntent(intent)
        val code = intent.data?.getQueryParameter("code")
        Log.d("MainActivity", "intent.data: ${intent.data}")
        if (code != null) {
            val authViewModel: AuthViewModel by viewModels()
            authViewModel.exchangeCodeForToken(code)
        }
    }
}