package com.example.core.data.repository

import com.example.core.data.Resource
import com.example.core.data.source.remote.RemoteDataSource
import com.example.core.data.source.remote.network.ApiResponse
import com.example.core.domain.model.News
import com.example.core.domain.repository.INewsRepository
import com.example.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class NewsRepository(private val remoteDataSource: RemoteDataSource) : INewsRepository {
    override fun getNews(query: String, language: String, apiKey: String) : Flow<Resource<News>> = flow{
        when(val response = remoteDataSource.getNews(query, language, apiKey).first()) {
            is ApiResponse.Success -> {
                val data = DataMapper.newsResponseToNews(response.data)
                emit(Resource.Success(data))
            }
            is ApiResponse.Error -> {
                emit(Resource.Error(response.message))
            }
            is ApiResponse.Empty -> {}
        }
    }
}