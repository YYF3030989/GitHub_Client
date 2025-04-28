package com.example.githubclient.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubclient.data.api.RetrofitClient
import com.example.githubclient.data.model.GithubUserDetail
import com.example.githubclient.repository.GithubRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GithubUserDetailViewModel(private val repository: GithubRepository): ViewModel() {

    private val _userDetail = MutableStateFlow<GithubUserDetail?>(null)
    val userDetail: StateFlow<GithubUserDetail?> = _userDetail

    fun loadUserDetail(username: String) {
        Log.d("GithubUserDetailViewModel", "Loading user detail for username: $username")
        viewModelScope.launch {
            try {
                val detail = repository.getUserDetail(username)
                _userDetail.value = detail
                Log.d("GithubUserDetailViewModel", "User detail loaded: $detail")
            } catch (e: Exception) {
                // You can add error handling here if needed
                Log.e("GithubUserDetailViewModel", "Error loading user detail", e)
                _userDetail.value = null
            }
        }
    }
}