package com.example.plantix.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.Resource
import com.example.core.data.source.remote.response.DetailUser
import com.example.core.domain.model.UserDetail
import com.example.core.domain.usecase.MainUseCase
import kotlinx.coroutines.launch

class ProfileViewModel(private val mainUseCase: MainUseCase) : ViewModel() {

    private val _detailUser = MutableLiveData<Resource<UserDetail>>()

    val detailUser: LiveData<Resource<UserDetail>> get() = _detailUser

    private val _updateProfile = MutableLiveData<Resource<UserDetail>>()

    val updateProfile: LiveData<Resource<UserDetail>> get() = _updateProfile

    fun deleteSession() {
        viewModelScope.launch {
            mainUseCase.deleteSession()
        }
    }

    fun getDetailUser(userId: Int) {
        viewModelScope.launch {
            _detailUser.value = Resource.Loading(true)
            mainUseCase.detailUser(userId).collect {
                _detailUser.value = it
            }
        }
    }

    fun updateProfileUser(userId: Int, data: DetailUser) {
        viewModelScope.launch {
            _updateProfile.value = Resource.Loading(true)
            mainUseCase.updateProfile(userId, data).collect {
                _updateProfile.value = it
            }
        }
    }
}