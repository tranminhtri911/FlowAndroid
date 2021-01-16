package com.example.flowmvvm.screen.searchUser

import androidx.lifecycle.viewModelScope
import com.example.flowmvvm.base.BaseViewModel
import com.example.flowmvvm.data.source.repositories.UserRepository
import com.example.flowmvvm.utils.LogUtils
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchUserViewModel
constructor(private val userRepository: UserRepository) : BaseViewModel() {

    fun fetchData() {

        viewModelScope.launch {
            userRepository.searchRepository("catch", 1)
                .catch { onError(it) }
                .collect {
                    LogUtils.d("userRepository", it.size.toString())
                }
        }
    }
}