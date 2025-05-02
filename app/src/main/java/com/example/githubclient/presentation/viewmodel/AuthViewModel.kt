package com.example.githubclient.presentation.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubclient.BuildConfig
import com.example.githubclient.core.util.TokenStore
import com.example.githubclient.core.network.GithubAuthConstants
import com.example.githubclient.data.model.DeviceCodeResponse
import com.example.githubclient.data.remote.api.GitHubAuthService
import com.example.githubclient.domin.repository.GithubAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    application: Application,
    private val repository: GithubAuthRepository
)  : AndroidViewModel(application) {

    private val _accessToken = MutableStateFlow<String?>(null)
    val accessToken: StateFlow<String?> = _accessToken

    private val _deviceCodeInfo = MutableStateFlow<DeviceCodeResponse?>(null)
    val deviceCodeInfo: StateFlow<DeviceCodeResponse?> = _deviceCodeInfo

    private var cancel = false

    init {
        viewModelScope.launch {
            TokenStore.accessTokenFlow(application.applicationContext).collect {
                _accessToken.value = it
            }
        }
    }

    fun getLoginIntent(): Intent? {
        val clientId = BuildConfig.GITHUB_CLIENT_ID
        val secret = BuildConfig.GITHUB_CLIENT_SECRET
        if (clientId.isBlank() || secret.isBlank()) return null
        val uri =
            "${GithubAuthConstants.TOKEN_URL}?client_id=${BuildConfig.GITHUB_CLIENT_ID}&redirect_uri=${GithubAuthConstants.REDIRECT_URI}&scope=repo".toUri()
        return Intent(Intent.ACTION_VIEW, uri)
    }

    fun startDeviceAuthorization() {
        viewModelScope.launch {
            val response = repository.getDeviceCode()
            _deviceCodeInfo.value = response
        }
    }

    fun pollForAccessToken(deviceCode: DeviceCodeResponse) {
        viewModelScope.launch {
            while (!cancel) {
                delay(deviceCode.interval * 1000L)
                try {
                    val response = repository.pollAccessToken(deviceCode.deviceCode)
                    _accessToken.value = response
                } catch (e : Exception){
                    Log.e("AuthViewModel", "Error polling for access token", e)
                }
            }
        }
    }

    fun exchangeCodeForToken(code: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAccessToken(code)
                _accessToken.value = response
                viewModelScope.launch {
                    TokenStore.saveToken(getApplication(), response)
                }
                Log.d("AuthViewModel", "Access Token: ${response}")
            } catch (e: Exception) {
                e.printStackTrace()
                _accessToken.value = null
            }
        }
    }

    fun cancelPolling(){
        cancel = true
    }

}