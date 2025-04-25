package com.example.githubclient.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.githubclient.data.api.RetrofitClient
import com.example.githubclient.repository.GithubRepository

class GithubUsersViewModel : ViewModel(){
    val users = GithubRepository(RetrofitClient.api).getPagedUsers().cachedIn(viewModelScope)
}