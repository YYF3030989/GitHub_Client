package com.example.githubclient.domin.repository

import com.example.githubclient.core.network.RateLimitException
import com.example.githubclient.data.model.ApiResult
import com.example.githubclient.domin.RateLimitManager
import retrofit2.Response

suspend inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> Response<T>
): ApiResult<T> {
    if (RateLimitManager.isRateLimited()) {
        return ApiResult.Error(RateLimitException(RateLimitManager.getRateLimitMessage()))
    }

    return try {
        val response = apiCall()
        RateLimitManager.updateFromHeaders(response.headers())

        if (response.code() == 403) {
            return ApiResult.Error(Exception("Rate limit exceeded. Try again later."))
        }

        if (response.isSuccessful) {
            response.body()?.let {
                ApiResult.Success(it)
            } ?: ApiResult.Error(Exception("Empty body"))
        } else {
            ApiResult.Error(Exception("HTTP ${response.code()}: ${response.message()}"))
        }
    } catch (e: Exception) {
        ApiResult.Error(e)
    }
}