package com.example.githubclient.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubclient.repository.GithubRepository
import com.example.githubclient.ui.viewmodel.GithubUserDetailViewModel

class GithubUserDetailFactory(private val repository: GithubRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GithubUserDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GithubUserDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}