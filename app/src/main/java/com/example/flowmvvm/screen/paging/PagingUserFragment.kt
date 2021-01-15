package com.example.flowmvvm.screen.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.flowmvvm.base.BaseFragment
import com.example.flowmvvm.databinding.FragmentPagingUserBinding
import kotlin.reflect.KClass

class PagingUserFragment : BaseFragment<FragmentPagingUserBinding, PagingUserViewModel>() {

    override val viewModelClass: KClass<PagingUserViewModel>
        get() = PagingUserViewModel::class

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPagingUserBinding
        get() = FragmentPagingUserBinding::inflate

    override fun setupView() {
    }

    override fun bindView() {
    }

    companion object {
        private const val TAG = "PagingUserFragment"
        fun newInstance() = PagingUserFragment()
    }
}