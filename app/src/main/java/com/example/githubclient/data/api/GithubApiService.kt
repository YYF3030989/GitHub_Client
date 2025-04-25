package com.example.githubclient.data.api

import com.example.githubclient.data.model.GithubUser
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {
    @GET("users")
    suspend fun getUserList(
        @Query("since") since: Int = 0,
        @Query("per_page") perPage: Int = 30
    ): Response<List<GithubUser>>
    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): Response<GithubUser>
}
