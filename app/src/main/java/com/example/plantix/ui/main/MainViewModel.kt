package com.example.plantix.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.Resource
import com.example.core.domain.usecase.MainUseCase
import kotlinx.coroutines.launch

class MainViewModel(private val mainUseCase: MainUseCase) : ViewModel() {

    private val _username = MutableLiveData<Resource<String>>()

    val username: LiveData<Resource<String>> get() = _username

    fun getUsername() {
        viewModelScope.launch {
            _username.value = Resource.Loading(true)
            mainUseCase.getUsername().collect {
                _username.value = Resource.Success(it)
            }
        }
    }
}