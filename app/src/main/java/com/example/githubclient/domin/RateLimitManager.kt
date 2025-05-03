package com.example.githubclient.domin

import okhttp3.Headers
import java.util.Date


object RateLimitManager {
    private var remaining: Int = Int.MAX_VALUE
    private var resetTime: Long = 0L

    fun updateFromHeaders(headers: Headers) {
        remaining = headers["X-RateLimit-Remaining"]?.toIntOrNull() ?: Int.MAX_VALUE
        resetTime = headers["X-RateLimit-Reset"]?.toLongOrNull()?.times(1000) ?: 0L
    }

    fun isRateLimited(): Boolean {
        val now = System.currentTimeMillis()
        return remaining == 0 && now < resetTime
    }

    fun getRateLimitMessage(): String {
        return if (resetTime > 0)
            "Rate limited until ${Date(resetTime)}"
        else
            "Rate limited"
    }
}
