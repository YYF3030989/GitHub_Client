package com.example.githubclient.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.githubclient.ui.viewmodel.GithubEventViewModel

@Composable
fun GithubUserDetailScreen(viewModel: GithubEventViewModel, username: String, login: String) {
    val events = viewModel.events.collectAsLazyPagingItems()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("username: $username")
        Text("Login: $login")
        Text("Events Nums: ${events.itemCount}")
    }
}