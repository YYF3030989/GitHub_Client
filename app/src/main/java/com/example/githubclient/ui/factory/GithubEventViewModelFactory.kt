package com.example.githubclient.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubclient.ui.viewmodel.GithubEventViewModel

class GithubEventViewModelFactory (private val login: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GithubEventViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GithubEventViewModel(login) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}