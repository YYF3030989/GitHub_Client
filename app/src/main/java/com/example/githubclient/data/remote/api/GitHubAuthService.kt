package com.example.githubclient.data.remote.api

import com.example.githubclient.core.network.GithubAuthConstants
import com.example.githubclient.data.model.AccessTokenResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface GitHubAuthService {
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