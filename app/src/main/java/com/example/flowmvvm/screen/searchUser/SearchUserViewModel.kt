package com.example.flowmvvm.screen.searchUser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.flowmvvm.base.BaseViewModel
import com.example.flowmvvm.base.paging.NetworkState
import com.example.flowmvvm.data.model.User
import com.example.flowmvvm.data.source.repositories.AppDBRepository
import com.example.flowmvvm.data.source.repositories.UserRepository
import com.example.flowmvvm.utils.Constants
import com.example.flowmvvm.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchUserViewModel
constructor(
        private val userRepository: UserRepository,
        private val appDBRepository: AppDBRepository) : BaseViewModel() {
    
    var networkState = MutableLiveData<NetworkState<MutableList<User>>>()
    
    fun initSearch(stateFlow: StateFlow<String>) {
        viewModelScope.launch {
            stateFlow
                    .debounce(300)
                    .distinctUntilChanged()
                    .flatMapLatest { query ->
                        networkState.value = NetworkState.FETCH
                        userRepository.searchRepository(query, Constants.API.PAGE_DEFAULT)
                                .catch {
                                    emit(emptyList())
                                    networkState.value = NetworkState.ERROR(it)
                                }
                    }
                    .map { it.toMutableList() }
                    .flowOn(Dispatchers.Main)
                    .collect {
                        networkState.value = NetworkState.SUCCESS(it)
                    }
        }
    }
    
    fun searchUser(query: String, nextPage: Int = Constants.API.PAGE_DEFAULT) {
        val isLoadMore = nextPage > 1
        networkState.value = if (isLoadMore) NetworkState.LOAD_MORE else NetworkState.FETCH
        
        viewModelScope.launch {
            userRepository.searchRepository(query, nextPage)
                    .catch { networkState.value = NetworkState.ERROR(it) }
                    .map { it.toMutableList() }
                    .flowOn(Dispatchers.Main)
                    .collect {
                        networkState.value = NetworkState.SUCCESS(it, isLoadMore)
                    }
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
}