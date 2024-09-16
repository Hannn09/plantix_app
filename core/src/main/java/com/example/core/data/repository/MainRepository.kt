package com.example.core.data.repository

import android.util.Log
import androidx.datastore.preferences.protobuf.Api
import com.example.core.data.Resource
import com.example.core.data.source.local.LocalDataSource
import com.example.core.data.source.remote.RemoteDataSource
import com.example.core.data.source.remote.network.ApiResponse
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
import com.example.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class MainRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IMainRepository {
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

    override fun detailUser(userId: Int): Flow<Resource<UserDetail>> = flow{
        when(val response = remoteDataSource.detailUser(userId).first()) {
            is ApiResponse.Success -> {
                val data = DataMapper.detailUsersResponseToDetailUser(response.data)
                emit(Resource.Success(data))
            }
            is ApiResponse.Error -> {
                emit(Resource.Error(response.message))
            }
            is ApiResponse.Empty -> {}
        }
    }

    override fun updateProfile(userId: Int, data: DetailUser): Flow<Resource<UserDetail>> = flow {
        when(val response = remoteDataSource.updateProfile(userId, data).first()) {
            is ApiResponse.Success -> {
                val data = DataMapper.updateUserResponseToUser(response.data)
                emit(Resource.Success(data))
            }
            is ApiResponse.Error -> {
                emit(Resource.Error(response.message))
            }
            is ApiResponse.Empty -> {}
        }
    }

    override fun detectionDisease(data: RequestDataDetection): Flow<Resource<DetectionDisease>> =
        flow {
            when (val response = remoteDataSource.detectionDisease(data).first()) {
                is ApiResponse.Success -> {
                    val data = DataMapper.requestDetectionResponse(response.data)
                    emit(Resource.Success(data))
                }

                is ApiResponse.Error -> {
                    emit(Resource.Error(response.message))
                }

                is ApiResponse.Empty -> {}
            }
        }

    override fun detailDetection(id: Int): Flow<Resource<DetailDisease>> = flow {
        when (val response = remoteDataSource.getDetailDetection(id).first()) {
            is ApiResponse.Success -> {
                val data = DataMapper.detailDetectionResponseToDetailDetection(response.data)
                emit(Resource.Success(data))
            }

            is ApiResponse.Error -> {
                emit(Resource.Error(response.message))
            }

            is ApiResponse.Empty -> {}
        }
    }

    override fun getUserDetection(userId: Int): Flow<Resource<ListDetection>> = flow {
        when (val response = remoteDataSource.getUserDetection(userId).first()) {
            is ApiResponse.Success -> {
                val data = DataMapper.userDetectionResponseToUserDetection(response.data)
                emit(Resource.Success(data))
            }

            is ApiResponse.Error -> {
                Log.d("ListDetection", "error: ${response.message}")
                emit(Resource.Error(response.message))
            }

            is ApiResponse.Empty -> {}
        }
    }

    override fun createPlant(userId: Int, name: String): Flow<Resource<PlantCreate>> = flow {
        when(val response = remoteDataSource.createPlant(userId, name).first()) {
            is ApiResponse.Success -> {
                val data = DataMapper.createPlantResponseToCreatePlant(response.data)
                emit(Resource.Success(data))
            }
            is ApiResponse.Error -> {
                Log.d("CreatePlant", "error: ${response.message}")
                emit(Resource.Error(response.message))
            }
            is ApiResponse.Empty -> {}
        }
    }

    override fun getUsername(): Flow<String> = localDataSource.getUsername()

    override fun getAuthToken(): Flow<String> = localDataSource.getSession()

    override suspend fun saveSession(token: String, username: String, userId: Int) {
        localDataSource.saveSession(token, username, userId)
    }

    override fun getUserId(): Flow<Int> = localDataSource.getUserId()

    override suspend fun deleteSession() {
        localDataSource.deleteSession()
    }
}