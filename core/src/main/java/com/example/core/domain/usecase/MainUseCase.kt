package com.example.core.domain.usecase

import com.example.core.data.Resource
import com.example.core.data.source.remote.response.DetailUser
import com.example.core.data.source.remote.response.RequestDataDetection
import com.example.core.domain.model.DetailDisease
import com.example.core.domain.model.DetectionDisease
import com.example.core.domain.model.ListDetection
import com.example.core.domain.model.Login
import com.example.core.domain.model.PlantCreate
import com.example.core.domain.model.Register
import com.example.core.domain.model.UserDetail
import kotlinx.coroutines.flow.Flow

interface MainUseCase {
    fun register(username: String, email: String, password: String) : Flow<Resource<Register>>

    fun login(username: String, password: String) : Flow<Resource<Login>>

    fun detailUser(userId: Int) : Flow<Resource<UserDetail>>

    fun updateProfile(userId: Int, data: DetailUser) : Flow<Resource<UserDetail>>

    fun detectionDisease(data: RequestDataDetection) : Flow<Resource<DetectionDisease>>

    fun detailDetection(id: Int) : Flow<Resource<DetailDisease>>

    fun getUserDetection(userId: Int) : Flow<Resource<ListDetection>>

    fun createPlant(userId: Int, name: String) : Flow<Resource<PlantCreate>>

    fun getUsername(): Flow<String>

    fun getAuthToken(): Flow<String>

    suspend fun saveSession(token: String, username: String, userId: Int)

    fun getUserId(): Flow<Int>

    suspend fun deleteSession()
}