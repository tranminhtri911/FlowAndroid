package com.example.flowmvvm.screen.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flowmvvm.base.BaseFragment
import com.example.flowmvvm.base.paging.NetworkState
import com.example.flowmvvm.base.recyclerView.OnItemClickListener
import com.example.flowmvvm.data.model.User
import com.example.flowmvvm.databinding.FragmentPagingUserBinding
import com.example.flowmvvm.screen.MainViewModel
import com.example.flowmvvm.utils.liveData.autoCleared
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import kotlin.reflect.KClass

class PagingUserFragment : BaseFragment<FragmentPagingUserBinding, PagingUserViewModel>() {
    
    override val viewModelClass: KClass<PagingUserViewModel>
        get() = PagingUserViewModel::class
    
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPagingUserBinding
        get() = FragmentPagingUserBinding::inflate
    
    private var adapter by autoCleared<PagingUserAdapter>()
    
    private val mainViewModel by sharedViewModel<MainViewModel>()
    
    override fun setupView() {
        adapter = PagingUserAdapter(object : OnItemClickListener<User> {
            override fun onItemClick(item: User, position: Int, view: View?) {
                viewModel.insertUser(item)
        
                coroutineScope.launch { mainViewModel.testBehaviorRelay.onNext(value = item) }
            }
        })
        
        with(binding.recyclerView) {
            adapter = this@PagingUserFragment.adapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }
    
    override fun bindView() {
        with(binding.swipeRefreshLayout) {
            setOnRefreshListener {
                isRefreshing = true
                viewModel.refreshPaging()
            }
        }
        
        viewModel.networkState.observe(this, { state ->
            adapter.setNetworkState(state)
            binding.swipeRefreshLayout.isRefreshing = false
    
            when (state) {
                is NetworkState.ERROR -> {
                    onHandleError(state.throwable)
                }
                else -> {
                }
            }
        })
        
        coroutineScope.launch {
            viewModel.userPager.collectLatest {
                adapter.submitData(it)
            }
        }
    }
    
    companion object {
        private const val TAG = "PagingUserFragment"
        fun newInstance() = PagingUserFragment()
    }
}