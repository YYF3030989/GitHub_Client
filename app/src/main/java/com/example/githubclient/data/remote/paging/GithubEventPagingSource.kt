package com.example.githubclient.data.remote.paging

import androidx.paging.PagingState
import com.example.githubclient.data.remote.api.GithubApiService
import com.example.githubclient.data.model.GithubEvent
import okhttp3.Headers
import retrofit2.Response

class GithubEventPagingSource(
    private val api: GithubApiService,
    private val username: String
) : BaseGithubPagingSource<Int, GithubEvent>() {

    override suspend fun executeRequest(params: LoadParams<Int>): Response<List<GithubEvent>> {
        val page = params.key ?: 1
        val perPage = params.loadSize
        return api.getEvents(username, page = page, perPage = perPage)
    }

    override fun getPreviousKey(params: LoadParams<Int>): Int? {
        val page = params.key ?: 1
        return if (page > 1) page - 1 else null
    }

    override fun getNextKey(
        headers: Headers,
        params: LoadParams<Int>
    ): Int? {
        val link = headers["Link"] ?: return null
        val regex = Regex("""<[^>]*[?&]page=(\d+)[^>]*>; rel="next"""")
        return regex.find(link)?.groupValues?.get(1)?.toIntOrNull()
    }

    override fun getRefreshKey(state: PagingState<Int, GithubEvent>): Int? {
        return null
    }
}