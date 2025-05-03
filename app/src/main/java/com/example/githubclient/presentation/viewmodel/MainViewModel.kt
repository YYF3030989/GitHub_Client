package com.example.githubclient.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubclient.core.util.TokenStore
import com.example.githubclient.data.model.ApiResult
import com.example.githubclient.data.model.GithubUser
import com.example.githubclient.domin.repository.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: GithubRepository) : ViewModel(){

    private val _currentUser = MutableStateFlow<GithubUser?>(null)
    val currentUser: StateFlow<GithubUser?> = _currentUser

    fun fetchAuthenticatedUser(context: Context) {
        viewModelScope.launch {
            val token = TokenStore.accessTokenFlow(context).firstOrNull()
            if (!token.isNullOrBlank()) {
                try {
                    val result = repository.getAuthenticatedUser()
                    if (result is ApiResult.Success) {
                        _currentUser.value = result.data
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    _currentUser.value = null
                }
            }
        }
    }
}