package com.example.githubclient

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.fetch.Fetcher
import com.example.githubclient.data.api.RetrofitClient
import com.example.githubclient.data.model.GithubUser
import com.example.githubclient.repository.GithubRepository
import com.example.githubclient.ui.component.GithubUserItem
import com.example.githubclient.ui.factory.GithubEventViewModelFactory
import com.example.githubclient.ui.factory.GithubUserDetailFactory
import com.example.githubclient.ui.screen.GithubUserDetailScreen
import com.example.githubclient.ui.screen.GithubUserListScreen
import com.example.githubclient.ui.theme.GithubClientTheme
import com.example.githubclient.ui.viewmodel.GithubEventViewModel
import com.example.githubclient.ui.viewmodel.GithubUserDetailViewModel
import com.example.githubclient.ui.viewmodel.GithubUsersViewModel
import kotlinx.coroutines.flow.flowOf

class MainActivity : ComponentActivity() {
    private lateinit var userViewModel: GithubUsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        userViewModel = GithubUsersViewModel()
        setContent {
            val navController = rememberNavController()

            GithubClientTheme {
                NavHost(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    startDestination = "users"
                ) {
                    composable("users") {
                        GithubUserListScreen(userViewModel, navController)
                    }
                    composable("user/{userId}/{login}") { backStackEntry ->
                        val login = backStackEntry.arguments?.getString("login")
                        val eventViewModel: GithubEventViewModel = viewModel(
                            factory = GithubEventViewModelFactory(login.toString())
                        )
                        val detailViewModel: GithubUserDetailViewModel = viewModel(
                            factory = GithubUserDetailFactory(GithubRepository(RetrofitClient.api))
                        )
                        GithubUserDetailScreen(login.toString(), detailViewModel, eventViewModel,
                            onBackClick = {
                                navController.popBackStack() // 🛬 this is the magic
                            })
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GithubUserListPreview() {
    val fakeData = remember { flowOf(fakeGithubUsers()) }
    val users = fakeData.collectAsLazyPagingItems()
    LazyColumn {
        items(users.itemCount, key = users.itemKey{ it.id }) { Index ->
            GithubUserItem(users[Index]!!, onClick = {
                Log.d("MainActivity", "User clicked: ${it.login}")
            })
        }
    }
}

fun fakeGithubUsers(): PagingData<GithubUser> {
    val sampleList = listOf(
        GithubUser(
            id = 1, login = "mojombo", avatar_url = "https://avatars.githubusercontent.com/u/1?v=4",
            node_id = TODO(),
            gravatar_id = TODO(),
            url = TODO(),
            html_url = TODO(),
            followers_url = TODO(),
            following_url = TODO(),
            gists_url = TODO(),
            starred_url = TODO(),
            subscriptions_url = TODO(),
            organizations_url = TODO(),
            repos_url = TODO(),
            events_url = TODO(),
            received_events_url = TODO(),
            type = TODO(),
            userViewType = TODO(),
            site_admin = TODO()
        ),
        GithubUser(
            id = 2, login = "defunkt", avatar_url = "https://avatars.githubusercontent.com/u/2?v=4",
            node_id = TODO(),
            gravatar_id = TODO(),
            url = TODO(),
            html_url = TODO(),
            followers_url = TODO(),
            following_url = TODO(),
            gists_url = TODO(),
            starred_url = TODO(),
            subscriptions_url = TODO(),
            organizations_url = TODO(),
            repos_url = TODO(),
            events_url = TODO(),
            received_events_url = TODO(),
            type = TODO(),
            userViewType = TODO(),
            site_admin = TODO()
        ),
        GithubUser(
            id = 3, login = "pjhyett", avatar_url = "https://avatars.githubusercontent.com/u/3?v=4",
            node_id = TODO(),
            gravatar_id = TODO(),
            url = TODO(),
            html_url = TODO(),
            followers_url = TODO(),
            following_url = TODO(),
            gists_url = TODO(),
            starred_url = TODO(),
            subscriptions_url = TODO(),
            organizations_url = TODO(),
            repos_url = TODO(),
            events_url = TODO(),
            received_events_url = TODO(),
            type = TODO(),
            userViewType = TODO(),
            site_admin = TODO()
        )
    )
    return PagingData.from(sampleList)
}
