package com.example.githubclient.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubclient.core.util.TokenStore
import com.example.githubclient.data.model.DeviceCodeResponse
import com.example.githubclient.domin.repository.GithubAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
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

    val isGuest = TokenStore.isGuestFlow(application)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private var cancel = false

    init {
        viewModelScope.launch {
            TokenStore.accessTokenFlow(application.applicationContext).collect {
                _accessToken.value = it
            }
        }
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

    fun cancelPolling(){
        cancel = true
    }

    fun loginAsGuest(context: Context) {
        viewModelScope.launch {
            TokenStore.setGuestMode(context, true)
            TokenStore.clearToken(context)
        }
    }

    fun logout(context: Context) {
        viewModelScope.launch {
            TokenStore.clearAll(context)
            TokenStore.setGuestMode(context, false)
            _accessToken.value = null
        }
    }
}