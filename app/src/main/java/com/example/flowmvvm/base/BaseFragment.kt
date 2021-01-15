package com.example.flowmvvm.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KClass

abstract class BaseFragment<B : ViewBinding, VM : BaseViewModel> : Fragment() {

    protected abstract val viewModelClass: KClass<VM>
    private val viewModel: VM by viewModel(clazz = viewModelClass)

    protected abstract val bindingInflater: (LayoutInflater, ViewGroup?) -> B
    private var _binding: B? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container)
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
        viewModel.tag = viewModelClass.simpleName.toString()

        viewModel.isLoading.observe(this, { isShow ->
            val parentActivity = (context as BaseActivity<*, *>)
            if (isShow) parentActivity.showLoading() else parentActivity.hideLoading()
        })

        viewModel.apiError.observe(this, {
            val parentActivity = (context as BaseActivity<*, *>)
            parentActivity.onHandleError(it)
        })
    }

    protected abstract fun setupView()

    protected abstract fun bindView()
}
