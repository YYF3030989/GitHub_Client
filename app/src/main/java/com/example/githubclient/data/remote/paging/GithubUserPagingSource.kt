package com.example.githubclient.data.remote.paging

import androidx.paging.PagingState
import com.example.githubclient.data.remote.api.GithubApiService
import com.example.githubclient.data.model.GithubUser
import okhttp3.Headers
import retrofit2.Response

class GithubUserPagingSource (
    private val api: GithubApiService
) : BaseGithubPagingSource<Int, GithubUser>() {

    override suspend fun executeRequest(params: LoadParams<Int>): Response<List<GithubUser>> {
        val since = params.key ?: 0
        val perPage = params.loadSize
        return api.getUserList(since = since, perPage = perPage)
    }

    override fun getPreviousKey(params: LoadParams<Int>): Int? = null

    override fun getNextKey(
        headers: Headers,
        params: LoadParams<Int>
    ): Int? {
        val link = headers["Link"] ?: return null
        val regex = Regex("""<[^>]*[?&]since=(\d+)[^>]*>; rel="next"""")
        return regex.find(link)?.groupValues?.get(1)?.toIntOrNull()
    }

    override fun getRefreshKey(state: PagingState<Int, GithubUser>): Int? = null
}