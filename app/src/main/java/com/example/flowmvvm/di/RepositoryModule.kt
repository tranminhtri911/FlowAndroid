package com.example.flowmvvm.di

import android.app.Application
import com.example.flowmvvm.data.source.local.dao.AppDatabase
import com.example.flowmvvm.data.source.local.sharedprf.SharedPrefsApi
import com.example.flowmvvm.data.source.local.sharedprf.SharedPrefsImpl
import com.example.flowmvvm.data.source.remote.service.ApiService
import com.example.flowmvvm.data.source.repositories.*
import com.google.gson.Gson
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val RepositoryModule = module {
    
    single { provideTokenRepository(androidApplication()) }
    
    single { provideAppDBRepository(get(), get()) }
    
    single { provideUserRepository(get(), get(), get()) }
}

fun provideTokenRepository(app: Application): TokenRepository {
    return TokenRepository(SharedPrefsImpl(app))
}

fun provideAppDBRepository(appDatabase: AppDatabase, gson: Gson): AppDBRepository {
    return AppDBRepositoryImpl(appDatabase, gson)
}

fun provideUserRepository(apiService: ApiService,
        sharedPrefsApi: SharedPrefsApi,
        gson: Gson): UserRepository {
    return UserRepositoryImpl(apiService, sharedPrefsApi, gson)
}