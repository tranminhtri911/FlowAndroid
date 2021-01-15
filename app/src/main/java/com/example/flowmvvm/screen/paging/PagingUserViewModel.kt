package com.example.flowmvvm.screen.paging

import com.example.flowmvvm.base.BaseViewModel
import com.example.flowmvvm.data.source.repositories.AppDBRepository
import com.example.flowmvvm.data.source.repositories.UserRepository

class PagingUserViewModel constructor(
    private val userRepository: UserRepository,
    private val appDBRepository: AppDBRepository
) : BaseViewModel() {
}