package com.example.flowmvvm.di

import com.example.flowmvvm.screen.MainViewModel
import com.example.flowmvvm.screen.favorite.FavoriteUserViewModel
import com.example.flowmvvm.screen.paging.PagingUserViewModel
import com.example.flowmvvm.screen.searchUser.SearchUserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val ViewModelModule: Module = module {
    viewModel { MainViewModel() }

    viewModel { SearchUserViewModel(get()) }

    viewModel { FavoriteUserViewModel(get(), get()) }

    viewModel { PagingUserViewModel(get()) }
}