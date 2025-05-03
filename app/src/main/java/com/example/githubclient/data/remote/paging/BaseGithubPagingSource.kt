package com.example.githubclient.data.remote.paging

import androidx.paging.PagingSource
import com.example.githubclient.domin.RateLimitManager
import okhttp3.Headers
import retrofit2.Response

abstract class BaseGithubPagingSource<Key : Any, Value : Any> : PagingSource<Key, Value>() {

    override suspend fun load(params: LoadParams<Key>): LoadResult<Key, Value> {
        // Skip network call if rate-limited
        if (RateLimitManager.isRateLimited()) {
            return LoadResult.Error(Exception(RateLimitManager.getRateLimitMessage()))
        }

        return try {
            val response = executeRequest(params)

            RateLimitManager.updateFromHeaders(response.headers())
            if (response.code() == 403) {
                return LoadResult.Error(Exception("Rate limit exceeded. Try again later."))
            }

            if (response.isSuccessful) {
                val body = response.body() ?: emptyList()
                LoadResult.Page(
                    data = body,
                    prevKey = getPreviousKey(params),
                    nextKey = getNextKey(response.headers(), params)
                )
            } else {
                LoadResult.Error(Exception("HTTP ${response.code()}: ${response.message()}"))
            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    protected abstract suspend fun executeRequest(params: LoadParams<Key>): Response<List<Value>>

    protected abstract fun getPreviousKey(params: LoadParams<Key>): Key?

    protected abstract fun getNextKey(headers: Headers, params: LoadParams<Key>): Key?
}
