package com.example.githubclient.ui.viewmodel

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubclient.BuildConfig
import com.example.githubclient.ui.common.GithubAuthConstants
import com.example.githubclient.data.TokenStore
import com.example.githubclient.data.api.GitHubAuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthViewModel(application: Application)  : AndroidViewModel(application) {

    private val _accessToken = MutableStateFlow<String?>(null)
    val accessToken: StateFlow<String?> = _accessToken

    init {
        viewModelScope.launch {
            TokenStore.accessTokenFlow(application.applicationContext).collect {
                _accessToken.value = it
            }
        }
    }

    private val authApi: GitHubAuthService = Retrofit.Builder()
        .baseUrl("https://github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GitHubAuthService::class.java)

    fun getLoginIntent(): Intent {
        val uri =
            "${GithubAuthConstants.TOKEN_URL}?client_id=${BuildConfig.GITHUB_CLIENT_ID}&redirect_uri=${GithubAuthConstants.REDIRECT_URI}&scope=repo".toUri()
        return Intent(Intent.ACTION_VIEW, uri)
    }

    fun exchangeCodeForToken(code: String) {
        viewModelScope.launch {
            try {
                val response = authApi.getAccessToken(
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