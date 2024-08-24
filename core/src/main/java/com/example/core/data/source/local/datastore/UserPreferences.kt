package com.example.core.data.source.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(token: String, username: String) = dataStore.edit { preferences ->
        preferences[TOKEN] = token
        preferences[USERNAME] = username
    }

    fun getUsername(): Flow<String> = dataStore.data.map { preferences ->
        preferences[USERNAME] ?: ""
    }

    fun getSession(): Flow<String> = dataStore.data.map { preferences ->
        preferences[TOKEN] ?: ""
    }

    suspend fun deleteSession() = dataStore.edit {
        it.clear()
    }

    companion object {
        private val TOKEN = stringPreferencesKey("token")
        private val USERNAME = stringPreferencesKey("username")
    }
}
