package com.example.core.data.repository

import com.example.core.data.Resource
import com.example.core.data.source.local.LocalDataSource
import com.example.core.data.source.remote.RemoteDataSource
import com.example.core.data.source.remote.network.ApiResponse
import com.example.core.domain.model.Login
import com.example.core.domain.model.Register
import com.example.core.domain.repository.IMainRepository
import com.example.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class MainRepository(private val remoteDataSource: RemoteDataSource, private val localDataSource: LocalDataSource) : IMainRepository {
    override fun register(
        username: String,
        email: String,
        password: String
    ): Flow<Resource<Register>> = flow {
        when (val response = remoteDataSource.register(username, email, password).first()) {
            is ApiResponse.Success -> {
                val data = DataMapper.registerResponseToRegister(response.data)
                emit(Resource.Success(data))
            }
            is ApiResponse.Error -> {
                emit(Resource.Error(response.message))
            }
            is ApiResponse.Empty -> {}
        }
    }

    override fun login(username: String, password: String): Flow<Resource<Login>> = flow {
        when (val response = remoteDataSource.login(username, password).first()) {
            is ApiResponse.Success -> {
                val data = DataMapper.loginResponseToLogin(response.data)
                emit(Resource.Success(data))
            }
            is ApiResponse.Error -> {
                emit(Resource.Error(response.message))
            }
            is ApiResponse.Empty -> {}
        }
    }

    override fun getUsername(): Flow<String> = localDataSource.getUsername()

    override fun getAuthToken(): Flow<String> = localDataSource.getSession()

    override suspend fun saveSession(token: String, username: String) {
       localDataSource.saveSession(token, username)
    }

    override suspend fun deleteSession() {
        localDataSource.deleteSession()
    }
}