package com.example.flowmvvm.utils.liveData

import com.example.flowmvvm.data.source.remote.api.error.RetrofitException


/**
 * A generic class that holds a value withScheduler its loading netWorkState.
 * @param <T>
</T> */
data class Resource<out T>(
    val netWorkState: NetWorkState,
    val data: T?,
    val error: RetrofitException?
) {

    companion object {

        fun <T> fetchData(data: T?): Resource<T> {
            return Resource(NetWorkState.FETCH, data, null)
        }

        fun <T> loadMore(data: T?): Resource<T> {
            return Resource(NetWorkState.LOAD_MORE, data, null)
        }

        fun <T> refreshData(data: T?): Resource<T> {
            return Resource(NetWorkState.REFRESH, data, null)
        }

        fun <T> error(error: RetrofitException): Resource<T> {
            return Resource(NetWorkState.ERROR, null, error)
        }

        fun <T> multiStatus(netWorkState: NetWorkState, data: T?): Resource<T> {
            return Resource(netWorkState, data, null)
        }
    }
}
