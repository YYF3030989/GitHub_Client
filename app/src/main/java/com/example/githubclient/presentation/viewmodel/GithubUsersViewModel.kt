package com.example.githubclient.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.cachedIn
import com.example.githubclient.core.network.RateLimitException
import com.example.githubclient.domin.repository.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class GithubUsersViewModel @Inject constructor(repository: GithubRepository) : ViewModel(){
    // Paging data exposed to UI
    val users = repository.getPagedUsers()
        .cachedIn(viewModelScope)

    // For displaying UI messages (e.g., rate limit, errors)
    private val _uiMessage = MutableStateFlow<String?>(null)
    val uiMessage: StateFlow<String?> = _uiMessage

    // Used to observe paging source errors
    fun handleLoadState(loadState: CombinedLoadStates) {
        val errorState = loadState.refresh as? LoadState.Error
            ?: loadState.append as? LoadState.Error
            ?: loadState.prepend as? LoadState.Error

        errorState?.let { loadError ->
            val message = when (val e = loadError.error) {
                is RateLimitException -> e.message ?: "Rate limit exceeded"
                else -> "Failed to load users: ${e.message}"
            }
            _uiMessage.value = message
        }
    }

    // Clear message (e.g. after snackbar shown)
    fun clearMessage() {
        _uiMessage.value = null
    }
}