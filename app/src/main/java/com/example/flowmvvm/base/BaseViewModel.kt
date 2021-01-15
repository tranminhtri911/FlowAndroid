package com.example.flowmvvm.base

import androidx.lifecycle.ViewModel
import com.example.flowmvvm.data.source.remote.api.error.RetrofitException
import com.example.flowmvvm.utils.Constant
import com.example.flowmvvm.utils.LogUtils
import com.example.flowmvvm.utils.liveData.SingleLiveEvent

abstract class BaseViewModel : ViewModel() {

    var tag: String = ""
    val isLoading: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent() }
    val apiError: SingleLiveEvent<RetrofitException> by lazy { SingleLiveEvent() }

    override fun onCleared() {
        super.onCleared()
        isLoading.clear()
        apiError.clear()

        LogUtils.d(tag, Constant.RELEASED)
    }
}