package com.example.githubclient.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.githubclient.domin.repository.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GithubUsersViewModel @Inject constructor(repository: GithubRepository) : ViewModel(){
    val users = repository.getPagedUsers().cachedIn(viewModelScope)
}