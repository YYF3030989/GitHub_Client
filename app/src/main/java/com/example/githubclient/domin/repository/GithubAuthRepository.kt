package com.example.githubclient.domin.repository

import com.example.githubclient.BuildConfig
import com.example.githubclient.data.model.DeviceCodeResponse
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

    suspend fun getDeviceCode(): DeviceCodeResponse {
        return api.getDeviceCode(BuildConfig.GITHUB_CLIENT_ID)
    }

    suspend fun pollAccessToken(deviceCode: String): String{
        return api.pollAccessToken(
            BuildConfig.GITHUB_CLIENT_ID,
            deviceCode
        ).accessToken
    }
}