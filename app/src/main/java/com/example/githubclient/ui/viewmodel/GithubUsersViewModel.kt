package com.example.githubclient.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.githubclient.repository.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GithubUsersViewModel @Inject constructor(private val repository: GithubRepository) : ViewModel(){
    val users = repository.getPagedUsers().cachedIn(viewModelScope)
}