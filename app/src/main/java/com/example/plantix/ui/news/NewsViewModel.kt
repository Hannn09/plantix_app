package com.example.plantix.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.Resource
import com.example.core.domain.model.News
import com.example.core.domain.usecase.NewsUseCase
import kotlinx.coroutines.launch

class NewsViewModel(private val newsUseCase: NewsUseCase) : ViewModel() {

    private val _news = MutableLiveData<Resource<News>>()

    val news: LiveData<Resource<News>> get() = _news

    private val _bannerNews = MutableLiveData<Resource<News>>()

    val bannerNews: LiveData<Resource<News>> get() = _bannerNews

    fun getNews(query: String, language: String, apiKey: String) {
        viewModelScope.launch {
            _news.value = Resource.Loading(true)
            newsUseCase.getNews(query, language, apiKey).collect {
                _news.value = it
            }
        }
    }

    fun getBannerNews(query: String, language: String, apiKey: String) {
        viewModelScope.launch {
            _bannerNews.value = Resource.Loading(true)
            newsUseCase.getNews(query, language, apiKey).collect {
                _bannerNews.value = it
            }
        }
    }

}