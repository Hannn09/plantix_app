package com.example.core.domain.usecase

import com.example.core.data.Resource
import com.example.core.domain.model.Login
import com.example.core.domain.model.Register
import com.example.core.domain.repository.IMainRepository
import kotlinx.coroutines.flow.Flow

class MainInteractor(private val mainRepository: IMainRepository) : MainUseCase {
    override fun register(username: String, email: String, password: String): Flow<Resource<Register>> = mainRepository.register(username, email, password)

    override fun login(username: String, password: String): Flow<Resource<Login>> = mainRepository.login(username, password)

    override fun getUsername(): Flow<String> = mainRepository.getUsername()

    override fun getAuthToken(): Flow<String> = mainRepository.getAuthToken()

    override suspend fun saveSession(token: String, username: String) = mainRepository.saveSession(token, username)

    override suspend fun deleteSession() = mainRepository.deleteSession()
}