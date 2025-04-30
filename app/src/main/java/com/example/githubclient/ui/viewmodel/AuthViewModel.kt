package com.example.githubclient.ui.viewmodel

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubclient.BuildConfig
import com.example.githubclient.data.TokenStore
import com.example.githubclient.data.api.GitHubAuthService
import com.example.githubclient.ui.common.GithubAuthConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    application: Application,
    private val authService: GitHubAuthService
)  : AndroidViewModel(application) {

    private val _accessToken = MutableStateFlow<String?>(null)
    val accessToken: StateFlow<String?> = _accessToken

    init {
        viewModelScope.launch {
            TokenStore.accessTokenFlow(application.applicationContext).collect {
                _accessToken.value = it
            }
        }
    }

    fun getLoginIntent(): Intent {
        val uri =
            "${GithubAuthConstants.TOKEN_URL}?client_id=${BuildConfig.GITHUB_CLIENT_ID}&redirect_uri=${GithubAuthConstants.REDIRECT_URI}&scope=repo".toUri()
        return Intent(Intent.ACTION_VIEW, uri)
    }

    fun exchangeCodeForToken(code: String) {
        viewModelScope.launch {
            try {
                val response = authService.getAccessToken(
                    BuildConfig.GITHUB_CLIENT_ID,
                    BuildConfig.GITHUB_CLIENT_SECRET,
                    code
                )
                _accessToken.value = response.accessToken
                viewModelScope.launch {
                    TokenStore.saveToken(getApplication(), response.accessToken)
                }
                Log.d("AuthViewModel", "Access Token: ${response.accessToken}")
            } catch (e: Exception) {
                e.printStackTrace()
                _accessToken.value = null
            }
        }
    }
}