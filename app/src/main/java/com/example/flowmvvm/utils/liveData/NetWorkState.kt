package com.example.flowmvvm.utils.liveData

/**
 * NetWorkState of a resource that is provided to the UI.
 *
 *
 * These are usually created by the Repository classes where they return
 * `LiveData<Resource<T>>` to pass back the latest data to the UI withScheduler its fetch netWorkState.
 */
enum class NetWorkState {
    FETCH,
    REFRESH,
    LOAD_MORE,
    SUCCESS,
    ERROR,
}
