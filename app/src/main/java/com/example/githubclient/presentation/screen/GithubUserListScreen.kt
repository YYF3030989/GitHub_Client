package com.example.githubclient.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.githubclient.R
import com.example.githubclient.presentation.component.GithubUserItem
import com.example.githubclient.presentation.viewmodel.GithubUsersViewModel
import com.example.githubclient.presentation.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GithubUserListScreen(
    viewModel: GithubUsersViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    navController: NavHostController,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    val users = viewModel.users.collectAsLazyPagingItems()
    val message by viewModel.uiMessage.collectAsState()

    LaunchedEffect(users.loadState) {
        viewModel.handleLoadState(users.loadState)
    }

    Scaffold(
        snackbarHost = {
            message?.let {
                Snackbar(
                    modifier = Modifier.padding(8.dp),
                    action = {
                        TextButton(onClick = { viewModel.clearMessage() }) {
                            Text(stringResource(R.string.ok))
                        }
                    }
                ) { Text(it) }
            }
        },
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar (
                title = {
                    Text (stringResource(R.string.github_user_list_topbar_text))
                },
                actions = {
                    IconButton(onClick = {
                        mainViewModel.fetchAuthenticatedUser(context)
                        showDialog = true
                    }) {
                        Icon(Icons.Default.ManageAccounts, contentDescription = stringResource(R.string.logout))
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(count = users.itemCount, key = users.itemKey { it.id }) { index ->
                GithubUserItem(user = users[index]!!, onClick = {
                    navController.navigate("user/${it.id}/${it.login}")
                })
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }

            when (users.loadState.append) {
                is LoadState.Loading -> item {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }

                is LoadState.Error -> item {
                    Text(stringResource(R.string.error_loading_more_items))
                }

                else -> {}
            }
        }
    }

    if (showDialog) {

        val user by mainViewModel.currentUser.collectAsState()
        AccountDialog(
            user = user,
            onDismiss = { showDialog = false },
            onLogout = onLogout
        )
    }
}