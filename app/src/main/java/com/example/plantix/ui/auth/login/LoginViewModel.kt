package com.example.plantix.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.Resource
import com.example.core.domain.model.Login
import com.example.core.domain.usecase.MainUseCase
import kotlinx.coroutines.launch

class LoginViewModel(private val mainUseCase: MainUseCase) : ViewModel() {

    private val _result = MutableLiveData<Resource<Login>>()

    val result: LiveData<Resource<Login>> get() = _result

    private val _token = MutableLiveData<Resource<String>>()

    val token: LiveData<Resource<String>> get() = _token

    fun login(username: String, password: String) {
        _result.value = Resource.Loading(true)
        viewModelScope.launch {
            mainUseCase.login(username, password).collect {
                _result.value = it
            }
        }
    }

    fun getSession() {
        _token.value = Resource.Loading(true)
        viewModelScope.launch {
            mainUseCase.getAuthToken().collect {
                _token.value = Resource.Success(it)
            }
        }
    }

    fun saveSession(token: String, username: String) {
        viewModelScope.launch {
            mainUseCase.saveSession(token, username)
        }
    }
}