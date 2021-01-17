package com.example.flowmvvm.data.source.dataSource

import android.os.Handler
import android.os.Looper
import androidx.paging.PagingSource
import com.example.flowmvvm.base.paging.NetworkState
import com.example.flowmvvm.utils.LogUtils
import retrofit2.HttpException
import java.io.IOException

/**
 * BaseDataSourceFactory
 *
 * @param <T> is Object
 *
 */

abstract class BaseDataSource<T : Any> : PagingSource<Int, T>() {
    
    private var listener: PagingSourceListener? = null
    
    private lateinit var runnable: Runnable
    
    private var handler: Handler? = Handler(Looper.getMainLooper())
    
    abstract suspend fun loadData(nextPage: Int): List<T>
    
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val nextPage = params.key ?: 1
        
        if (nextPage == 1) {
            updateState(NetworkState.FETCH)
        } else {
            updateState(NetworkState.LOAD_MORE)
        }
        
        return try {
            val data: List<T> = loadData(nextPage = nextPage)
            
            updateState(NetworkState.SUCCESS(null))
            
            LoadResult.Page(
                    data = data,
                    prevKey = if (nextPage == 1) null else nextPage - 1,
                    nextKey = nextPage + 1
            )
        } catch (exception: IOException) {
            updateState(NetworkState.ERROR(exception))
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            updateState(NetworkState.ERROR(exception))
            LoadResult.Error(exception)
        }
    }
    
    fun registerListener(pagingSourceListener: PagingSourceListener) {
        listener = pagingSourceListener
    }
    
    fun onClear() {
        listener = null
        
        handler?.removeCallbacks(runnable)
        handler = null
    }
    
    fun onRefresh() {
        onClear()
        invalidate()
    }
    
    private fun updateState(state: NetworkState<Any>) {
        LogUtils.d("NetworkState", state.toString())
        
        runnable = Runnable { listener?.onNetWorkStateChange(state) }
        handler?.postDelayed(runnable, 100)
    }
}
