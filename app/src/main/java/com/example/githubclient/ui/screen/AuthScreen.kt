package com.example.githubclient.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.githubclient.ui.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    navController: NavController, authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val token by authViewModel.accessToken.collectAsState()

    // Navigate to main screen if token is available
//    LaunchedEffect(token) {
//        Log.d("AuthScreen", "Token: $token")
//        if (!token.isNullOrEmpty()) {
//            navController.navigate("users") {
//                popUpTo("auth") { inclusive = true } // Remove AuthScreen from back stack
//            }
//        }
//    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                val intent = authViewModel.getLoginIntent()
                context.startActivity(intent)
            }
        ) {
            Text(text = "Login with GitHub")
        }
    }
}

