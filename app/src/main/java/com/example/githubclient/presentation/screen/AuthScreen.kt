package com.example.githubclient.presentation.screen

import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
    val GITHUB_DEVICE_LOGIN_URL = "https://github.com/login/device"

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
                    text = stringResource(R.string.device_code_input_instructor),
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
                    modifier = Modifier.width(220.dp),
                    onClick = {
                        authViewModel.pollForAccessToken(deviceCodeInfo!!)
                        showDialog = true
                        val uri = GITHUB_DEVICE_LOGIN_URL.toUri()

                        val customTabsIntent = CustomTabsIntent.Builder()
                            .setShowTitle(true)
                            .build()
                        customTabsIntent.launchUrl(context, uri)
                    }
                ) {
                    Text(text = stringResource(R.string.github_login_button))
                }

                Spacer(Modifier.height(8.dp))

                OutlinedButton(
                    modifier = Modifier.width(220.dp),
                    onClick = {
                        authViewModel.loginAsGuest(context)
                        onLoginSuccess()
                    }
                ) {
                    Text(stringResource(R.string.login_as_guest))
                }

            } else {
                CircularProgressIndicator()
                Text(stringResource(R.string.initializing_github_login))
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = {
                        authViewModel.cancelPolling()
                        showDialog = false
                    },
                    title = { Text(stringResource(R.string.logging_in)) },
                    text = {
                        Column {
                            CircularProgressIndicator()
                            Spacer(Modifier.height(8.dp))
                            Text(stringResource(R.string.browser_login_waiting_text))
                        }
                    },
                    confirmButton = {},
                    dismissButton = {
                        TextButton(onClick = {
                            authViewModel.cancelPolling()
                            showDialog = false
                        }) {
                            Text(text = stringResource(R.string.cancel))
                        }
                    }
                )
            }
        }
    }
}

