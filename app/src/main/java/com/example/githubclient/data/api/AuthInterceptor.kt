package com.example.githubclient.data.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Accept", "application/vnd.github+json")
            .addHeader("Authorization", "Bearer $token")
            .addHeader("X-GitHub-Api-Version", "2022-11-28")
            .build()
        return chain.proceed(request)
    }
}