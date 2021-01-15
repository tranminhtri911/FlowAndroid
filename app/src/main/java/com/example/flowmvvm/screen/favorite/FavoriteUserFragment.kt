package com.example.flowmvvm.screen.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.flowmvvm.base.BaseFragment
import com.example.flowmvvm.databinding.FragmentFavoriteUserBinding
import kotlin.reflect.KClass

class FavoriteUserFragment : BaseFragment<FragmentFavoriteUserBinding, FavoriteUserViewModel>() {

    override val viewModelClass: KClass<FavoriteUserViewModel>
        get() = FavoriteUserViewModel::class

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFavoriteUserBinding
        get() = FragmentFavoriteUserBinding::inflate

    override fun setupView() {
    }

    override fun bindView() {
    }

    companion object {
        private const val TAG = "FavoriteUserFragment"
        fun newInstance() = FavoriteUserFragment()
    }
}