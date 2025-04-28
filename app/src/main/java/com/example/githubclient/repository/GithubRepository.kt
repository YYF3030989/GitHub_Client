package com.example.githubclient.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.githubclient.data.Paging.GithubEventPagingSource
import com.example.githubclient.data.Paging.GithubUserPagingSource
import com.example.githubclient.data.api.GithubApiService
import com.example.githubclient.data.model.GithubEvent
import com.example.githubclient.data.model.GithubUser
import com.example.githubclient.data.model.GithubUserDetail
import kotlinx.coroutines.flow.Flow

class GithubRepository(private val api: GithubApiService) {
    fun getPagedUsers(): Flow<PagingData<GithubUser>> {
        return Pager(
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = { GithubUserPagingSource(api) }
        ).flow
    }

    suspend fun getUserDetail(username: String): GithubUserDetail {
        return api.getUser(username)
    }

    fun getPagedEvent(username: String): Flow<PagingData<GithubEvent>> {
        return Pager(
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = { GithubEventPagingSource(api, username) }
        ).flow
    }
}