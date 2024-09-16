package com.example.core.data.source.remote

import android.util.Log
import androidx.datastore.preferences.protobuf.Api
import com.example.core.data.source.remote.network.ApiResponse
import com.example.core.data.source.remote.network.MainApiService
import com.example.core.data.source.remote.network.NewsApiService
import com.example.core.data.source.remote.response.DetailDiseaseResponse
import com.example.core.data.source.remote.response.DetailUser
import com.example.core.data.source.remote.response.DetectionDiseaseResponse
import com.example.core.data.source.remote.response.ListDetectionResponse
import com.example.core.data.source.remote.response.LoginResponse
import com.example.core.data.source.remote.response.NewsResponse
import com.example.core.data.source.remote.response.PlantCreateResponse
import com.example.core.data.source.remote.response.RegisterResponse
import com.example.core.data.source.remote.response.RequestDataDetection
import com.example.core.data.source.remote.response.UserDetailResponse
import com.example.core.utils.DataMapper
import com.example.core.utils.DataMapper.parseErrorMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException

class RemoteDataSource(
    private val apiService: NewsApiService,
    private val mainApiService: MainApiService
) {
    suspend fun getNews(
        query: String,
        language: String,
        apiKey: String
    ): Flow<ApiResponse<NewsResponse>> = flow {
        try {
            val response = apiService.getNews(query, language, apiKey)
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
        } catch (e: IOException) {
            emit(ApiResponse.Error("Tidak ada koneksi internet. Silakan coba lagi"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun register(
        username: String,
        email: String,
        password: String
    ): Flow<ApiResponse<RegisterResponse>> = flow {
        try {
            val response = mainApiService.register(username, email, password)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            if (e.code() == 400) {
                val errorMessage =
                    "Username atau email sudah terpakai. Silakan gunakan username atau email lain."
                emit(ApiResponse.Error(errorMessage))
            } else {
                emit(ApiResponse.Error("Terjadi kesalahan. Silakan coba lagi"))
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error("Terjadi kesalahan. Silakan coba lagi"))
        } catch (e: IOException) {
            emit(ApiResponse.Error("Tidak ada koneksi internet. Silakan coba lagi"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun login(username: String, password: String): Flow<ApiResponse<LoginResponse>> = flow {
        try {
            val response = mainApiService.login(username, password)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            if (e.code() == 401) {
                val errorMessage = "Username atau password salah. Silakan coba lagi."
                emit(ApiResponse.Error(errorMessage))
            } else {
                emit(ApiResponse.Error("Terjadi kesalahan. Silakan coba lagi"))
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error("Terjadi kesalahan. Silakan coba lagi"))
        } catch (e: IOException) {
            emit(ApiResponse.Error("Tidak ada koneksi internet. Silakan coba lagi"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun detailUser(userId: Int): Flow<ApiResponse<UserDetailResponse>> = flow {
        try {
            val response = mainApiService.detailUser(userId)
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            Log.d("UsersDetail", "error: ${e.message}")
            emit(ApiResponse.Error("Terjadi kesalahan. Silakan coba lagi"))
        } catch (e: IOException) {
            emit(ApiResponse.Error("Tidak ada koneksi internet. Silakan coba lagi"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun updateProfile(
        userId: Int,
        data: DetailUser
    ): Flow<ApiResponse<UserDetailResponse>> = flow {
        try {
            val user = DataMapper.updateUserMapping(data)
            val response = mainApiService.updateProfileUser(userId, user)
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            Log.d("UsersDetail", "error: ${e.message}")
            emit(ApiResponse.Error("Terjadi kesalahan. Silakan coba lagi"))
        } catch (e: IOException) {
            emit(ApiResponse.Error("Tidak ada koneksi internet. Silakan coba lagi"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun detectionDisease(
        detection: RequestDataDetection
    ): Flow<ApiResponse<DetectionDiseaseResponse>> = flow {
        try {
            val data = DataMapper.requestDetection(detection)
            val response = mainApiService.detectionDisease(data)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = parseErrorMessage(errorBody)
            Log.d("DetectionDisease", "error: $errorMessage")
            emit(ApiResponse.Error("Tanaman gagal di deteksi!Silahkan coba lagi nanti"))
        } catch (e: Exception) {
            Log.d("DetectionDisease", "error: ${e.message}")
            emit(ApiResponse.Error("Tanaman gagal di deteksi!Silahkan coba lagi nanti"))
        } catch (e: IOException) {
            emit(ApiResponse.Error("Tidak ada koneksi internet. Silakan coba lagi"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getDetailDetection(id: Int): Flow<ApiResponse<DetailDiseaseResponse>> = flow {
        try {
            val response = mainApiService.getDetailDetection(id)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = parseErrorMessage(errorBody)
            Log.d("DetailDetection", "error: $errorMessage")
            emit(ApiResponse.Error(errorMessage))
        } catch (e: Exception) {
            Log.d("DetailDetection", "error: ${e.message}")
            emit(ApiResponse.Error("Terjadi kesalahan. Silakan coba lagi"))
        } catch (e: IOException) {
            emit(ApiResponse.Error("Tidak ada koneksi internet. Silakan coba lagi"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getUserDetection(userId: Int): Flow<ApiResponse<ListDetectionResponse>> = flow {
        try {
            val response = mainApiService.getUserDetection(userId)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = parseErrorMessage(errorBody)
            Log.d("ListUserDetection", "error: $errorMessage")
            emit(ApiResponse.Error(errorMessage))
        } catch (e: Exception) {
            Log.d("ListUserDetection", "error: ${e.message}")
            emit(ApiResponse.Error("Terjadi kesalahan. Silakan coba lagi"))
        } catch (e: IOException) {
            emit(ApiResponse.Error("Tidak ada koneksi internet. Silakan coba lagi"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun createPlant(userId: Int, name: String): Flow<ApiResponse<PlantCreateResponse>> =
        flow<ApiResponse<PlantCreateResponse>> {
            try {
                val response = mainApiService.createPlant(userId, name)
                emit(ApiResponse.Success(response))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = parseErrorMessage(errorBody)
                Log.d("PlantCreate", "error: $errorMessage")
                emit(ApiResponse.Error("Tanaman gagal di deteksi!Silahkan coba lagi nanti"))
            } catch (e: Exception) {
                Log.d("PlantCreate", "error: ${e.message}")
                emit(ApiResponse.Error("Tanaman gagal di deteksi!Silahkan coba lagi nanti"))
            } catch (e: IOException) {
                emit(ApiResponse.Error("Tidak ada koneksi internet. Silakan coba lagi"))
            }
        }.flowOn(Dispatchers.IO)
}