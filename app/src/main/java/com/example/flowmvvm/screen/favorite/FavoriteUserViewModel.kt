package com.example.flowmvvm.screen.favorite

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.example.flowmvvm.base.BaseViewModel
import com.example.flowmvvm.data.model.User
import com.example.flowmvvm.data.source.repositories.AppDBRepository
import com.example.flowmvvm.data.source.repositories.UserRepository
import com.example.flowmvvm.utils.LogUtils
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoriteUserViewModel constructor(
        private val userRepository: UserRepository,
        private val appDBRepository: AppDBRepository
) : BaseViewModel() {
    
    val users = MediatorLiveData<MutableList<User>>()
    
    init {
        viewModelScope.launch {
            LogUtils.d("FavoriteUserViewModel", "Fetch User Local")
    
            users.addSource(appDBRepository.getUsers()) { response ->
                LogUtils.d("FavoriteUserViewModel", "Fetch User Local Success")
    
                users.value = response
            }
        }
    }
    
    fun deleteUser(user: User) {
        viewModelScope.launch {
            appDBRepository.deleteUser(user)
                    .collectLatest {
                        LogUtils.d("Delete User Succeeded: ", user.fullName.toString())
                    }
        }
    }
}