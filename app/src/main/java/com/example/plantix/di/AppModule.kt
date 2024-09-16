package com.example.plantix.di

import com.example.core.domain.usecase.MainInteractor
import com.example.core.domain.usecase.MainUseCase
import com.example.core.domain.usecase.NewsInteractor
import com.example.core.domain.usecase.NewsUseCase
import com.example.plantix.ui.auth.login.LoginViewModel
import com.example.plantix.ui.auth.register.RegisterViewModel
import com.example.plantix.ui.main.MainViewModel
import com.example.plantix.ui.news.NewsViewModel
import com.example.plantix.ui.profile.ProfileViewModel
import com.example.plantix.ui.scan.DetectionViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<NewsUseCase> { NewsInteractor(get()) }
    factory<MainUseCase> { MainInteractor(get()) }
}

val viewModelModule = module {
    viewModel { NewsViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { DetectionViewModel(get()) }
}