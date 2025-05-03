package com.example.githubclient.core.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

object TokenStore {
    private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    private val IS_GUEST_KEY = booleanPreferencesKey("is_guest")
    private var cachedToken: String? = null
    private var cachedGuest: Boolean = false

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

    suspend fun setGuestMode(context: Context, isGuest: Boolean) {
        cachedGuest = isGuest
        context.dataStore.edit { prefs ->
            prefs[IS_GUEST_KEY] = isGuest
        }
    }

    val isGuestFlow: (Context) -> Flow<Boolean> = { context ->
        context.dataStore.data.map { prefs ->
            val isGuest = prefs[IS_GUEST_KEY] ?: false
            cachedGuest = isGuest
            isGuest
        }
    }

    suspend fun clearAll(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}