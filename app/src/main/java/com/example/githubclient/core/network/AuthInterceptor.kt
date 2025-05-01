package com.example.githubclient.core.network

import com.example.githubclient.core.util.TokenStore
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = TokenStore.getCachedToken()
        val request = if (!token.isNullOrEmpty()) {
            chain.request().newBuilder()
                .addHeader("Accept", "application/vnd.github+json")
                .addHeader("Authorization", "Bearer $token")
                .addHeader("X-GitHub-Api-Version", "2022-11-28")
                .build()
        } else {
            chain.request().newBuilder()
                .addHeader("Accept", "application/vnd.github+json")
                .addHeader("X-GitHub-Api-Version", "2022-11-28")
                .build()
        }
        return chain.proceed(request)
    }
}