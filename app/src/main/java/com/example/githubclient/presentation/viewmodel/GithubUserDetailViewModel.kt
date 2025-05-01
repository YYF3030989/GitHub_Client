package com.example.githubclient.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubclient.data.model.GithubUserDetail
import com.example.githubclient.domin.repository.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubUserDetailViewModel @Inject constructor(private val repository: GithubRepository): ViewModel() {

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
                Log.e("GithubUserDetailViewModel", "Error loading user detail", e)
                _userDetail.value = null
            }
        }
    }
}