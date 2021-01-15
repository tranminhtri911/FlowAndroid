package com.example.flowmvvm.data.source.repositories

import com.example.flowmvvm.data.source.local.dao.AppDatabase
import com.google.gson.Gson

interface AppDBRepository

class AppDBRepositoryImpl
constructor(private val appDB: AppDatabase, private val gson: Gson) : AppDBRepository
