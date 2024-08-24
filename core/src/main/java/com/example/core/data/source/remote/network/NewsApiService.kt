package com.example.core.data.source.remote.network

import com.example.core.data.source.remote.response.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v2/everything")
    suspend fun getNews(
        @Query("q") query: String,
        @Query("language") language: String,
        @Query("apiKey") apiKey: String
    ) : NewsResponse
}