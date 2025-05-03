package com.example.githubclient.domin.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.githubclient.data.model.ApiResult
import com.example.githubclient.data.model.GithubEvent
import com.example.githubclient.data.model.GithubUser
import com.example.githubclient.data.model.GithubUserDetail
import com.example.githubclient.data.remote.api.GithubApiService
import com.example.githubclient.data.remote.paging.GithubEventPagingSource
import com.example.githubclient.data.remote.paging.GithubUserPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubRepository @Inject constructor(private val api: GithubApiService) {
    fun getPagedUsers(): Flow<PagingData<GithubUser>> {
        return Pager(
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = { GithubUserPagingSource(api) }
        ).flow
    }

    suspend fun getUserDetail(username: String): ApiResult<GithubUserDetail> = safeApiCall {
        api.getUser(username)
    }

    fun getPagedEvent(username: String): Flow<PagingData<GithubEvent>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                initialLoadSize = 30),
            pagingSourceFactory = { GithubEventPagingSource(api, username) }
        ).flow
    }

    suspend fun getAuthenticatedUser(): ApiResult<GithubUser> = safeApiCall {
        api.getAuthenticatedUser()
    }
}