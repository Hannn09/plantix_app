package com.example.core.data.source.remote

import com.example.core.data.source.remote.network.ApiResponse
import com.example.core.data.source.remote.network.MainApiService
import com.example.core.data.source.remote.network.NewsApiService
import com.example.core.data.source.remote.response.LoginResponse
import com.example.core.data.source.remote.response.NewsResponse
import com.example.core.data.source.remote.response.RegisterResponse
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
}