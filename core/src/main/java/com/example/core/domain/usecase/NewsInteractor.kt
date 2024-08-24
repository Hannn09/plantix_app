package com.example.core.domain.usecase

import com.example.core.data.Resource
import com.example.core.domain.model.News
import com.example.core.domain.repository.INewsRepository
import kotlinx.coroutines.flow.Flow

class NewsInteractor(private val newsRepository: INewsRepository): NewsUseCase {
    override fun getNews(query: String, language: String, apiKey: String): Flow<Resource<News>> = newsRepository.getNews(query, language, apiKey)
}