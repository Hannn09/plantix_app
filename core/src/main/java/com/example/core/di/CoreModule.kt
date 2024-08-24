package com.example.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.core.data.source.remote.network.MainApiService
import com.example.core.data.source.remote.network.NewsApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.example.core.BuildConfig
import com.example.core.data.repository.MainRepository
import com.example.core.data.repository.NewsRepository
import com.example.core.data.source.local.LocalDataSource
import com.example.core.data.source.local.datastore.UserPreferences
import com.example.core.data.source.remote.RemoteDataSource
import com.example.core.domain.repository.IMainRepository
import com.example.core.domain.repository.INewsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_NEWS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(NewsApiService::class.java)
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(MainApiService::class.java)
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

fun provideDataStore(context: Context): DataStore<Preferences> { return context.dataStore }

val repositoryModule = module {
    single { provideDataStore(androidContext())}
    single { UserPreferences(get()) }
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get(), get()) }
    single<INewsRepository> { NewsRepository(get()) }
    single<IMainRepository> { MainRepository(get(), get()) }
}