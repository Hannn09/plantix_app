package com.example.plantix.ui.auth.login

import android.util.Log
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

    private val _userId = MutableLiveData<Resource<Int>>()

    val userId: LiveData<Resource<Int>> get() = _userId

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

    fun saveSession(token: String, username: String, userId: Int) {
        viewModelScope.launch {
            mainUseCase.saveSession(token, username, userId)
        }
    }

    fun getUserId() {
        viewModelScope.launch {
            _userId.value = Resource.Loading(true)
            mainUseCase.getUserId().collect {
                _userId.value = Resource.Success(it)
                Log.d("GetUserID", "id: $it")
            }
        }
    }
}