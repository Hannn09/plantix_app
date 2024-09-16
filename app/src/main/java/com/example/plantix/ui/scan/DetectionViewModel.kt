package com.example.plantix.ui.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.Resource
import com.example.core.data.source.remote.response.RequestDataDetection
import com.example.core.domain.model.DetailDisease
import com.example.core.domain.model.DetectionDisease
import com.example.core.domain.model.ListDetection
import com.example.core.domain.model.PlantCreate
import com.example.core.domain.usecase.MainUseCase
import kotlinx.coroutines.launch

class DetectionViewModel(private val mainUseCase: MainUseCase) : ViewModel() {

    private val _detectionResult = MutableLiveData<Resource<DetectionDisease>>()

    val detectionResult: LiveData<Resource<DetectionDisease>> get() = _detectionResult

    private val _detectionDetail = MutableLiveData<Resource<DetailDisease>>()

    val detectionDetail: LiveData<Resource<DetailDisease>> get() = _detectionDetail

    private val _listUserDetection = MutableLiveData<Resource<ListDetection>>()

    val listUserDetection: LiveData<Resource<ListDetection>> get() = _listUserDetection

    private val _createPlant = MutableLiveData<Resource<PlantCreate>>()

    val createPlant: LiveData<Resource<PlantCreate>> get() = _createPlant

    fun detectionDisease(data: RequestDataDetection) {
        viewModelScope.launch {
            _detectionResult.value = Resource.Loading(true)
            mainUseCase.detectionDisease(data).collect {
                _detectionResult.value = it
            }
        }
    }

    fun detailDetection(id: Int) {
        viewModelScope.launch {
            _detectionDetail.value = Resource.Loading(true)
            mainUseCase.detailDetection(id).collect {
                _detectionDetail.value = it
            }
        }
    }

    fun getUserDetection(userId: Int) {
        viewModelScope.launch {
            _listUserDetection.value = Resource.Loading(true)
            mainUseCase.getUserDetection(userId).collect {
                _listUserDetection.value = it
            }
        }
    }

    fun createPlant(userId: Int, name: String) {
        viewModelScope.launch {
            _createPlant.value = Resource.Loading(true)
            mainUseCase.createPlant(userId, name).collect {
                _createPlant.value = it
            }
        }
    }
}