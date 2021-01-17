package com.example.flowmvvm.base

import androidx.lifecycle.ViewModel
import com.example.flowmvvm.utils.Constants
import com.example.flowmvvm.utils.LogUtils
import com.example.flowmvvm.utils.liveData.SingleLiveEvent

abstract class BaseViewModel : ViewModel() {
    
    var tag: String = ""
    val loadingEvent: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent() }
    val errorEvent: SingleLiveEvent<Throwable> by lazy { SingleLiveEvent() }
    
    fun setLoading(isShow: Boolean) {
        loadingEvent.value = isShow
    }
    
    fun onError(error: Throwable) {
        errorEvent.value = error
    }
    
    override fun onCleared() {
        super.onCleared()
        loadingEvent.clear()
        errorEvent.clear()
        
        LogUtils.d(tag, Constants.RELEASED)
    }
}