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
import com.example.core.domain.repository.IMainRepository
import kotlinx.coroutines.flow.Flow

class MainInteractor(private val mainRepository: IMainRepository) : MainUseCase {
    override fun register(username: String, email: String, password: String): Flow<Resource<Register>> = mainRepository.register(username, email, password)

    override fun login(username: String, password: String): Flow<Resource<Login>> = mainRepository.login(username, password)
    override fun detailUser(userId: Int): Flow<Resource<UserDetail>> = mainRepository.detailUser(userId)
    override fun updateProfile(userId: Int, data: DetailUser): Flow<Resource<UserDetail>> = mainRepository.updateProfile(userId, data)
    override fun detectionDisease(data: RequestDataDetection): Flow<Resource<DetectionDisease>> = mainRepository.detectionDisease(data)
    override fun detailDetection(id: Int): Flow<Resource<DetailDisease>> = mainRepository.detailDetection(id)
    override fun getUserDetection(userId: Int): Flow<Resource<ListDetection>> = mainRepository.getUserDetection(userId)
    override fun createPlant(userId: Int, name: String): Flow<Resource<PlantCreate>> = mainRepository.createPlant(userId, name)
    override fun getUsername(): Flow<String> = mainRepository.getUsername()

    override fun getAuthToken(): Flow<String> = mainRepository.getAuthToken()

    override suspend fun saveSession(token: String, username: String, userId: Int) = mainRepository.saveSession(token, username, userId)
    override fun getUserId(): Flow<Int> = mainRepository.getUserId()

    override suspend fun deleteSession() = mainRepository.deleteSession()
}