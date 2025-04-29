package com.example.githubclient.data.api

import com.example.githubclient.data.model.GithubEvent
import com.example.githubclient.data.model.GithubUser
import com.example.githubclient.data.model.GithubUserDetail
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
    suspend fun getUser(@Path("username") username: String): GithubUserDetail

    @GET("users/{username}/events/public")
    suspend fun getEvents(
        @Path("username") username: String,
        @Query("since") since: Int = 1,
        @Query("per_page") perPage: Int = 10
    ): Response<List<GithubEvent>>

    @GET("user")
    suspend fun getAuthenticatedUser(): GithubUser
}
