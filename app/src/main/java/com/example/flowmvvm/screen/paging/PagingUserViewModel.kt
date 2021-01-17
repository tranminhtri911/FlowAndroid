package com.example.flowmvvm.screen.paging

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import com.example.flowmvvm.base.BaseViewModel
import com.example.flowmvvm.base.paging.BassePagingDataAdapter
import com.example.flowmvvm.base.paging.NetworkState
import com.example.flowmvvm.data.model.User
import com.example.flowmvvm.data.source.dataSource.UserDataSource
import com.example.flowmvvm.data.source.repositories.AppDBRepository
import com.example.flowmvvm.data.source.repositories.UserRepository
import com.example.flowmvvm.utils.LogUtils
import com.example.flowmvvm.utils.extension.notNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PagingUserViewModel
constructor(
        private val userRepository: UserRepository,
        private val appDBRepository: AppDBRepository) : BaseViewModel() {
    
    private  var dataSource: UserDataSource? = null
    
    var networkState = MediatorLiveData<NetworkState<Any>>()
    
    val userPager: Flow<PagingData<User>> by lazy {
        Pager(config = BassePagingDataAdapter.config,
                pagingSourceFactory = {
                    LogUtils.d("refreshPaging", "newPager")
                    val source = UserDataSource("catch", userRepository)
                    dataSource = source
                    networkState.addSource(source.netWorkState) { networkState.value = it }
                    return@Pager source
                })
                .flow
    }
    
    fun refreshPaging() {
        dataSource.notNull {
            networkState.removeSource(it.netWorkState)
            it.onRefresh()
        }
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
        dataSource.notNull {
            networkState.removeSource(it.netWorkState)
            it.onClear()
        }
        dataSource = null
        super.onCleared()
    }
}