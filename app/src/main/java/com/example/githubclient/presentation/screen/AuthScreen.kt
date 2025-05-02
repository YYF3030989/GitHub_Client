package com.example.githubclient.presentation.screen

import android.content.Intent
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.githubclient.R
import com.example.githubclient.core.util.TokenStore
import com.example.githubclient.presentation.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val token by authViewModel.accessToken.collectAsState()
    val deviceCodeInfo by authViewModel.deviceCodeInfo.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(token) {
        if (!token.isNullOrEmpty()) {
            TokenStore.saveToken(context, token!!)
            onLoginSuccess()
        }
    }

    LaunchedEffect(Unit) {
        authViewModel.startDeviceAuthorization()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (deviceCodeInfo != null) {
                Text(
                    text = "To authorize, please open the link below and enter the code:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = deviceCodeInfo!!.userCode,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = deviceCodeInfo!!.verificationUri,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        authViewModel.pollForAccessToken(deviceCodeInfo!!)
                        showDialog = true
//                val intent = authViewModel.getLoginIntent()
//                if (intent == null) {
//                    Toast.makeText(context,
//                        context.getString(R.string.github_para_error_msg),
//                        Toast.LENGTH_LONG).show()
//                    return@Button
//                }
//                context.startActivity(intent)
                        val uri = "https://github.com/login/device".toUri()


                        val customTabsIntent = CustomTabsIntent.Builder()
                            .setShowTitle(true)
                            .build()
                        customTabsIntent.launchUrl(context, uri)
                    }
                ) {
                    Text(text = stringResource(R.string.github_login_button))
                }

                Spacer(Modifier.height(8.dp))

                OutlinedButton(onClick = { onLoginSuccess() }) {
                    Text("Continue as Guest")
                }

            } else {
                CircularProgressIndicator()
                Text("Initializing GitHub login...")
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = {
                        authViewModel.cancelPolling()
                        showDialog = false
                    },
                    title = { Text("Logging in...") },
                    text = {
                        Column {
                            CircularProgressIndicator()
                            Spacer(Modifier.height(8.dp))
                            Text("Waiting for authorization. Please complete login in your browser.")
                        }
                    },
                    confirmButton = {},
                    dismissButton = {
                        TextButton(onClick = {
                            authViewModel.cancelPolling()
                            showDialog = false
                        }) {
                            Text(text = "Cancel")
                        }
                    }
                )
            }
        }
    }
}

