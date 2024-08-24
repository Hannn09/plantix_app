package com.example.core.domain.repository

import com.example.core.data.Resource
import com.example.core.domain.model.News
import kotlinx.coroutines.flow.Flow

interface INewsRepository {
    fun getNews(query: String, language: String, apiKey: String) : Flow<Resource<News>>
}