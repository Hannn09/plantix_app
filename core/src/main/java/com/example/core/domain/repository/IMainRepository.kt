package com.example.core.domain.repository

import com.example.core.data.Resource
import com.example.core.domain.model.Login
import com.example.core.domain.model.Register
import kotlinx.coroutines.flow.Flow

interface IMainRepository {
    fun register(username: String, email: String, password: String) : Flow<Resource<Register>>

    fun login(username: String, password: String) : Flow<Resource<Login>>

    fun getUsername(): Flow<String>

    fun getAuthToken(): Flow<String>

    suspend fun saveSession(token: String, username: String)

    suspend fun deleteSession()
}