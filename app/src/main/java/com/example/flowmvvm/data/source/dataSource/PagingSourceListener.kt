package com.example.flowmvvm.data.source.dataSource

import com.example.flowmvvm.base.paging.NetworkState

interface PagingSourceListener {
    fun onNetWorkStateChange(state: NetworkState)
}