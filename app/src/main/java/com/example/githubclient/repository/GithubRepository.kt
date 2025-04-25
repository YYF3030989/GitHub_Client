package com.example.githubclient.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.githubclient.data.GithubUserPagingSource
import com.example.githubclient.data.api.GithubApiService
import com.example.githubclient.data.api.RetrofitClient
import com.example.githubclient.data.model.GithubUser
import kotlinx.coroutines.flow.Flow

class GithubRepository(private val api: GithubApiService) {
    suspend fun getUser(username: String): Result<GithubUser> {
        return try {
            val response = RetrofitClient.api.getUser(username)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty body"))
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserList(): Result<List<GithubUser>> {
        return try {
            val response = RetrofitClient.api.getUserList()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty body"))
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getPagedUsers(): Flow<PagingData<GithubUser>> {
        return Pager(
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = { GithubUserPagingSource(api) }
        ).flow
    }
}