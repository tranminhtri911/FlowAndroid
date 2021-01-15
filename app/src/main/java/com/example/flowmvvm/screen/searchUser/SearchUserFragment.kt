package com.example.flowmvvm.screen.searchUser

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.flowmvvm.base.BaseFragment
import com.example.flowmvvm.databinding.FragmentSearchUserBinding
import kotlin.reflect.KClass

class SearchUserFragment : BaseFragment<FragmentSearchUserBinding, SearchUserViewModel>() {

    override val viewModelClass: KClass<SearchUserViewModel>
        get() = SearchUserViewModel::class

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchUserBinding
        get() = FragmentSearchUserBinding::inflate

    override fun setupView() {
    }

    override fun bindView() {
    }

    companion object {
        private const val TAG = "SearchUserFragment"
        fun newInstance() = SearchUserFragment()
    }
}