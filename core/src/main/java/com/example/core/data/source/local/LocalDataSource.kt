package com.example.core.data.source.local

import com.example.core.data.source.local.datastore.UserPreferences

class LocalDataSource(private val userPreferences: UserPreferences) {
    fun getSession() = userPreferences.getSession()

    fun getUsername() =  userPreferences.getUsername()

    fun getUserId() = userPreferences.getUserId()

    suspend fun saveSession(token: String, username: String, userId: Int) = userPreferences.saveSession(token, username, userId)

    suspend fun deleteSession() = userPreferences.deleteSession()
}