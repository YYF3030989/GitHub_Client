package com.example.githubclient.data.remote.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubclient.data.remote.api.GithubApiService
import com.example.githubclient.data.model.GithubEvent

class GithubEventPagingSource(
    private val api: GithubApiService,
    private val username: String
) : PagingSource<Int, GithubEvent>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubEvent> {
        val page = params.key ?: 1
        val perPage = params.loadSize

        return try {
            val response = api.getEvents(username = username, page = page, perPage = perPage)
            if (response.isSuccessful) {
                val events = response.body() ?: emptyList()
                val nextPage = extractNextFromLink(response.headers()["Link"])
                Log.d("GithubEventPagingSource", "nextPage: $nextPage")
                LoadResult.Page(
                    data = events,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = nextPage
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

    private fun extractNextFromLink(linkHeader: String?): Int? {
        if (linkHeader == null) return null
        val regex = Regex("""<[^>]*[?&]page=(\d+)[^>]*>; rel="next"""")
        return regex.find(linkHeader)?.groupValues?.get(1)?.toIntOrNull()
    }
}