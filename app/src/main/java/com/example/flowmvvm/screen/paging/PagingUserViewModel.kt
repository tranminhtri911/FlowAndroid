package com.example.flowmvvm.screen.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingData
import com.example.flowmvvm.base.BaseViewModel
import com.example.flowmvvm.base.paging.BassePagingDataAdapter
import com.example.flowmvvm.base.paging.NetworkState
import com.example.flowmvvm.data.model.User
import com.example.flowmvvm.data.source.dataSource.PagingSourceListener
import com.example.flowmvvm.data.source.dataSource.UserDataSource
import com.example.flowmvvm.data.source.repositories.UserRepository
import kotlinx.coroutines.flow.Flow

class PagingUserViewModel
constructor(private val userRepository: UserRepository) : BaseViewModel() {
    
    private var userDataSource: UserDataSource? = null
    
    var networkState: MutableLiveData<NetworkState> = MutableLiveData()
    
    val userPager: Flow<PagingData<User>> by lazy {
        Pager(config = BassePagingDataAdapter.config,
                pagingSourceFactory = {
                    makePagingSourceFactory()
                })
                .flow
    }
    
    private fun makePagingSourceFactory(): UserDataSource {
        return UserDataSource("catch", userRepository).apply {
            userDataSource = this
            registerListener(object : PagingSourceListener {
                override fun onNetWorkStateChange(state: NetworkState) {
                    networkState.value = state
                }
            })
        }
    }
    
    fun refreshPaging() {
        userDataSource?.onRefresh()
    }
    
    override fun onCleared() {
        userDataSource?.onClear()
        super.onCleared()
    }
}