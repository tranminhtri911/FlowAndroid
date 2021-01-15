package com.example.flowmvvm.screen.searchUser

import com.example.flowmvvm.base.BaseViewModel
import com.example.flowmvvm.data.source.repositories.UserRepository

class SearchUserViewModel
constructor(private val userRepository: UserRepository) : BaseViewModel() {
}