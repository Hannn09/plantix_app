package com.example.plantix.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.Resource
import com.example.core.domain.model.Register
import com.example.core.domain.usecase.MainUseCase
import kotlinx.coroutines.launch

class RegisterViewModel(private val mainUseCase: MainUseCase) : ViewModel() {

    private val _result = MutableLiveData<Resource<Register>>()

    val result: LiveData<Resource<Register>> get() = _result

    fun register(username: String, email: String, password: String) {
       viewModelScope.launch {
           _result.value = Resource.Loading(true)
           mainUseCase.register(username, email, password).collect {
               _result.value = it
           }
       }
    }

}