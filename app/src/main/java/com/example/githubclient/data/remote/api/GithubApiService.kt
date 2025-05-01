package com.example.githubclient.data.remote.api

import com.example.githubclient.core.network.GithubAuthConstants
import com.example.githubclient.data.model.AccessTokenResponse
import com.example.githubclient.data.model.GithubEvent
import com.example.githubclient.data.model.GithubUser
import com.example.githubclient.data.model.GithubUserDetail
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
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
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30
    ): Response<List<GithubEvent>>

    @GET("user")
    suspend fun getAuthenticatedUser(): GithubUser

    @FormUrlEncoded
    @POST("login/oauth/access_token")
    @Headers("Accept: application/json")
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String = GithubAuthConstants.REDIRECT_URI
    ): AccessTokenResponse
}
