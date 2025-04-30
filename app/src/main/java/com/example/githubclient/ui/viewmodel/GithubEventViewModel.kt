package com.example.githubclient.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.githubclient.repository.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GithubEventViewModel @Inject constructor(
    repository: GithubRepository,
    savedStateHandle: SavedStateHandle) : ViewModel() {
    private val username: String = savedStateHandle["login"] ?: ""
    val events = repository.getPagedEvent(username).cachedIn(viewModelScope)

}