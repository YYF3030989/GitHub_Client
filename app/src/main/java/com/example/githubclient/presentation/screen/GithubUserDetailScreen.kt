package com.example.githubclient.presentation.screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.example.githubclient.R
import com.example.githubclient.presentation.component.GithubEventItem
import com.example.githubclient.presentation.viewmodel.GithubEventViewModel
import com.example.githubclient.presentation.viewmodel.GithubUserDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GithubUserDetailScreen(
    username: String,
    userDetailViewModel: GithubUserDetailViewModel = hiltViewModel(),
    userEventsViewModel: GithubEventViewModel = hiltViewModel(),
    onBackClick : () -> Unit
) {
    val events = userEventsViewModel.events.collectAsLazyPagingItems()
    val detail = userDetailViewModel.userDetail.collectAsState()
    val isEndReached = events.loadState.append is LoadState.NotLoading && events.loadState.append.endOfPaginationReached


    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(username) {
        Log.d("GithubUserDetailScreen", "LaunchedEffect triggered with username: $username")
        userDetailViewModel.loadUserDetail(username)
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically){
                        AsyncImage(
                            model = detail.value?.avatarUrl,
                            contentDescription = stringResource(R.string.user_avatar),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                text = detail.value?.login ?: stringResource(R.string.text_placeholder),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        LazyColumn (modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .padding(innerPadding)
        ) {
            item {
                UserDetailHeader(detail.value)
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
            when (events.loadState.refresh) {
                is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                is LoadState.NotLoading -> {
                    if (events.itemCount == 0){
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.no_events_recently),
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    } else {
                        items(
                            count = events.itemCount,
                            key = events.itemKey { it.id }) { index ->
                            GithubEventItem(event = events[index]!!)
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                        }

                        if (events.loadState.append is LoadState.Loading) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }

                        if (events.loadState.append is LoadState.Error) {
                            item {
                                Text("Error loading more items. Tap to retry.")
                            }
                        }

                        if (isEndReached && events.itemCount > 0) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = stringResource(R.string.end_indicator),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }

                is LoadState.Error -> {
                    item {
                        Text(
                            text = stringResource(R.string.error_loading_events),
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}