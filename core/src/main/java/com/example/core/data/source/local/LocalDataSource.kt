package com.example.core.data.source.local

import com.example.core.data.source.local.datastore.UserPreferences

class LocalDataSource(private val userPreferences: UserPreferences) {
    fun getSession() = userPreferences.getSession()

    fun getUsername() =  userPreferences.getUsername()

    suspend fun saveSession(token: String, username: String) = userPreferences.saveSession(token, username)

    suspend fun deleteSession() = userPreferences.deleteSession()
}