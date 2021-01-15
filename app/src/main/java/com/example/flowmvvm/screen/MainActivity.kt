package com.example.flowmvvm.screen

import android.view.LayoutInflater
import com.example.flowmvvm.base.BaseActivity
import com.example.flowmvvm.databinding.ActivityMainBinding
import kotlin.reflect.KClass

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val viewModelClass: KClass<MainViewModel>
        get() = MainViewModel::class

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override fun setupView() {

    }

    override fun bindView() {

    }

}