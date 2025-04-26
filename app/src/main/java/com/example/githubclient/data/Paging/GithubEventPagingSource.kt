package com.example.githubclient.data.Paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubclient.data.api.GithubApiService
import com.example.githubclient.data.model.GithubEvent

class GithubEventPagingSource(
    private val api: GithubApiService,
    private val username: String
) : PagingSource<Int, GithubEvent>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubEvent> {
        val since = params.key ?: 0 // start from 0 if no key
        val perPage = params.loadSize

        return try {
            val response = api.getEvents(username = username, since = since, perPage = perPage)
            if (response.isSuccessful) {
                val events = response.body() ?: emptyList()
                val nextSince = extractSinceFromLink(response.headers()["Link"])
                LoadResult.Page(
                    data = events,
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

    override fun getRefreshKey(state: PagingState<Int, GithubEvent>): Int? {
        return null
    }

    private fun extractSinceFromLink(LinkHeader: String?): Int? {
        if (LinkHeader == null) return null
        val regex = Regex("""<[^>]*[?&]since=(\d+)[^>]*>; rel="next"""")
        return regex.find(LinkHeader)?.groupValues?.get(1)?.toIntOrNull()
    }
}