package com.example.flowmvvm.screen.paging

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import com.example.flowmvvm.base.BaseViewModel
import com.example.flowmvvm.base.paging.BassePagingDataAdapter
import com.example.flowmvvm.base.paging.NetworkState
import com.example.flowmvvm.data.model.User
import com.example.flowmvvm.data.source.dataSource.PagingSourceListener
import com.example.flowmvvm.data.source.dataSource.UserDataSource
import com.example.flowmvvm.data.source.repositories.AppDBRepository
import com.example.flowmvvm.data.source.repositories.UserRepository
import com.example.flowmvvm.utils.LogUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PagingUserViewModel
constructor(
        private val userRepository: UserRepository,
        private val appDBRepository: AppDBRepository) : BaseViewModel() {
    
    private var userDataSource: UserDataSource? = null
    
    var networkState: MutableLiveData<NetworkState<Any>> = MutableLiveData()
    
    val userPager: Flow<PagingData<User>> by lazy {
        Pager(config = BassePagingDataAdapter.config,
                pagingSourceFactory = {
                    makePagingSourceFactory()
                })
                .flow
    }
    
    private fun makePagingSourceFactory(): UserDataSource {
        LogUtils.d("refreshPaging", "makePagingSourceFactory")
        return UserDataSource("catch", userRepository).apply {
            userDataSource = this
            registerListener(object : PagingSourceListener {
                override fun onNetWorkStateChange(state: NetworkState<Any>) {
                    networkState.value = state
                }
            })
        }
    }
    
    fun refreshPaging() {
        userDataSource?.onRefresh()
    }
    
    fun insertUser(user: User) {
        viewModelScope.launch {
            appDBRepository.insertUser(user)
                    .collectLatest {
                        LogUtils.d("Insert User Succeeded: ", user.fullName.toString())
                    }
        }
    }
    
    override fun onCleared() {
        userDataSource?.onClear()
        super.onCleared()
    }
}