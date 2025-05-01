package com.example.githubclient.domin.repository

import com.example.githubclient.BuildConfig
import com.example.githubclient.data.remote.api.GitHubAuthService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubAuthRepository @Inject constructor(private val api: GitHubAuthService) {
    suspend fun getAccessToken(code: String): String{
        return api.getAccessToken(
            BuildConfig.GITHUB_CLIENT_ID,
            BuildConfig.GITHUB_CLIENT_SECRET,
            code
        ).accessToken
    }
}