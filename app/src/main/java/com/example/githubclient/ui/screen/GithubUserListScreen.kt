package com.example.githubclient.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.githubclient.ui.component.GithubUserItem
import com.example.githubclient.ui.viewmodel.GithubUsersViewModel

@Composable
fun GithubUserListScreen(viewModel: GithubUsersViewModel, navController: NavHostController) {
    val users = viewModel.users.collectAsLazyPagingItems()
    LazyColumn(
        modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        items(count = users.itemCount, key = users.itemKey { it.id }) { index ->
            GithubUserItem(user = users[index]!!, onClick = {
                Log.d("MainActivity", "User clicked: ${it.login}")
                navController.navigate("user/${it.id}/${it.login}")
            })
        }

        when (users.loadState.append) {
            is LoadState.Loading -> item {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }

            is LoadState.Error -> item {
                Text("Error loading more items")
            }

            else -> {}
        }
    }
}