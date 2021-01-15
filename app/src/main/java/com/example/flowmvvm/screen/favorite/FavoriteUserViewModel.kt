package com.example.flowmvvm.screen.favorite

import com.example.flowmvvm.base.BaseViewModel
import com.example.flowmvvm.data.source.repositories.AppDBRepository
import com.example.flowmvvm.data.source.repositories.UserRepository

class FavoriteUserViewModel constructor(
    private val userRepository: UserRepository,
    private val appDBRepository: AppDBRepository
) : BaseViewModel() {
}