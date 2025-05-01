package com.example.githubclient.core.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

object TokenStore {
    private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    private var cachedToken: String? = null
    suspend fun saveToken(context: Context, token: String) {
        cachedToken = token
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = token
        }
    }

    val accessTokenFlow: (Context) -> Flow<String?> = { context ->
        context.dataStore.data.map { prefs ->
            val token = prefs[ACCESS_TOKEN_KEY]
            cachedToken = token
            token
        }
    }

    suspend fun clearToken(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN_KEY)
        }
    }

    fun getCachedToken(): String? = cachedToken
}