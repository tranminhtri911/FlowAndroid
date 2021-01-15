package com.example.flowmvvm.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.flowmvvm.data.source.remote.api.error.RetrofitException
import com.example.flowmvvm.utils.extension.notNull
import com.example.flowmvvm.utils.extension.showToast
import com.example.flowmvvm.widgets.dialogManager.DialogManager
import com.example.flowmvvm.widgets.dialogManager.DialogManagerImpl
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KClass

abstract class BaseActivity<B : ViewBinding, VM : BaseViewModel> : AppCompatActivity() {

    protected abstract val viewModelClass: KClass<VM>
    protected val viewModel: VM by viewModel(clazz = viewModelClass)

    protected abstract val bindingInflater: (LayoutInflater) -> B
    protected val binding: B by lazy { bindingInflater.invoke(layoutInflater) }

    private val dialogManager: DialogManager by lazy { DialogManagerImpl(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bindViewModel()
        setupView()
        bindView()
    }

    override fun onDestroy() {
        dialogManager.onRelease()
        super.onDestroy()
    }

    private fun bindViewModel() {
        viewModel.tag = viewModelClass.simpleName.toString()

        viewModel.isLoading.observe(this, { isShow ->
            if (isShow) showLoading() else hideLoading()
        })

        viewModel.apiError.observe(this, {
            onHandleError(it)
        })
    }

    fun showLoading() {
        dialogManager.showLoading()
    }

    fun hideLoading() {
        dialogManager.hideLoading()
    }

    fun onHandleError(error: RetrofitException?) {
        error?.getMessageError().notNull { showToast(it) }
    }

    protected abstract fun setupView()

    protected abstract fun bindView()
}