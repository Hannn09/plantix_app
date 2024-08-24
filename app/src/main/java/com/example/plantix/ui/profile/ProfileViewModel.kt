package com.example.plantix.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.MainUseCase
import kotlinx.coroutines.launch

class ProfileViewModel(private val mainUseCase: MainUseCase) : ViewModel() {

    fun deleteSession() {
        viewModelScope.launch {
            mainUseCase.deleteSession()
        }
    }
}