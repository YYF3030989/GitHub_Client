package com.example.githubclient.data.Paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubclient.data.api.GithubApiService
import com.example.githubclient.data.model.GithubUser

class GithubUserPagingSource (
    private val api: GithubApiService
) : PagingSource<Int, GithubUser>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubUser> {
        val since = params.key ?: 0 // start from 0 if no key
        val perPage = params.loadSize

        return try {
            val response = api.getUserList(since = since, perPage = perPage)
            if (response.isSuccessful) {
                val users = response.body() ?: emptyList()
                val nextSince = extractSinceFromLink(response.headers()["Link"])
                LoadResult.Page(
                    data = users,
                    prevKey = null, // GitHub API does not support backward paging
                    nextKey = nextSince
                )
            } else {
                LoadResult.Error(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GithubUser>): Int? = null

    private fun extractSinceFromLink(linkHeader: String?): Int? {
        if (linkHeader == null) return null
        val regex = Regex("""<[^>]*[?&]since=(\d+)[^>]*>; rel="next"""")
        return regex.find(linkHeader)?.groupValues?.get(1)?.toIntOrNull()
    }
}