package com.example.githubclient.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubclient.data.TokenStore
import com.example.githubclient.data.model.GithubUser
import com.example.githubclient.repository.GithubRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MainViewModel(private val repository: GithubRepository) : ViewModel(){

    private val _currentUser = MutableStateFlow<GithubUser?>(null)
    val currentUser: StateFlow<GithubUser?> = _currentUser

    fun fetchAuthenticatedUser(context: Context) {
        viewModelScope.launch {
            val token = TokenStore.accessTokenFlow(context).firstOrNull()
            if (!token.isNullOrBlank()) {
                try {
                    val user = repository.getAuthenticatedUser()
                    _currentUser.value = user
                } catch (e: Exception) {
                    e.printStackTrace()
                    _currentUser.value = null
                }
            }
        }
    }

    fun logout(context: Context) {
        viewModelScope.launch {
            TokenStore.clearToken(context)
            _currentUser.value = null
        }
    }
}