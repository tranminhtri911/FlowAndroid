package com.example.flowmvvm.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KClass

abstract class BaseFragment<B : ViewBinding, VM : BaseViewModel> : Fragment() {
    
    protected abstract val viewModelClass: KClass<VM>
    val viewModel: VM by viewModel(clazz = viewModelClass)
    
    protected abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> B
    private var _binding: B? = null
    val binding get() = _binding!!
    
    protected val coroutineScope: LifecycleCoroutineScope
        get() = viewLifecycleOwner.lifecycleScope
    
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        setupView()
        bindView()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    private fun bindViewModel() {
        with(viewModel) {
            tag = viewModelClass.simpleName.toString()
            
            loadingEvent.observe(this@BaseFragment, {
                setLoading(it)
            })
            
            errorEvent.observe(this@BaseFragment, {
                onHandleError(it)
            })
        }
    }
    
    fun setLoading(isShow: Boolean) {
        val parentActivity = (context as BaseActivity<*, *>)
        parentActivity.setLoading(isShow)
    }
    
    fun onHandleError(throwable: Throwable) {
        val parentActivity = (context as BaseActivity<*, *>)
        parentActivity.onHandleError(throwable)
    }
    
    protected abstract fun setupView()
    
    protected abstract fun bindView()
}
