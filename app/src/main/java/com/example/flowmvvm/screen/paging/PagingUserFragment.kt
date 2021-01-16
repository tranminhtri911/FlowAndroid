package com.example.flowmvvm.screen.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flowmvvm.base.BaseFragment
import com.example.flowmvvm.base.paging.Status
import com.example.flowmvvm.base.recyclerView.OnItemClickListener
import com.example.flowmvvm.data.model.User
import com.example.flowmvvm.databinding.FragmentPagingUserBinding
import com.example.flowmvvm.utils.LogUtils
import com.example.flowmvvm.utils.extension.notNull
import com.example.flowmvvm.utils.liveData.autoCleared
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class PagingUserFragment : BaseFragment<FragmentPagingUserBinding, PagingUserViewModel>() {
    
    override val viewModelClass: KClass<PagingUserViewModel>
        get() = PagingUserViewModel::class
    
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPagingUserBinding
        get() = FragmentPagingUserBinding::inflate
    
    private var adapter by autoCleared<PagingUserAdapter>()
    
    override fun setupView() {
        adapter = PagingUserAdapter(object : OnItemClickListener<User> {
            override fun onItemClick(item: User, position: Int, view: View?) {
                LogUtils.d("onItemClick", item.fullName.toString())
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
            LogUtils.d("NetworkState", state.toString())
            adapter.setNetworkState(state)
    
            when (state.status) {
                Status.FETCH -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                Status.FAILED -> {
                    state.error.notNull { onHandleError(it) }
                }
                else -> {
                }
            }
        })
        
        viewLifecycleOwner.lifecycleScope.launch {
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