package com.example.flowmvvm.data.source.repositories

import com.example.flowmvvm.data.model.User
import com.example.flowmvvm.data.source.local.sharedprf.SharedPrefsApi
import com.example.flowmvvm.data.source.local.sharedprf.SharedPrefsKey
import com.example.flowmvvm.data.source.remote.api.response.ApiResponse
import com.example.flowmvvm.data.source.remote.service.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull

interface UserRepository {
    
    fun searchRepository(query: String, page: Int): Flow<List<User>>
    
    suspend fun searchRepositoryPaging(query: String, page: Int): ApiResponse<List<User>>
    
    fun saveUserToLocal(user: User)
    
    fun getUserFromLocal(): User?
}

class UserRepositoryImpl
constructor(
        private val apiService: ApiService,
        private val sharedPrefsApi: SharedPrefsApi
) : UserRepository {
    
    private val gson = Gson()
    
    override fun searchRepository(query: String, page: Int): Flow<List<User>> {
        return flow { emit(apiService.searchRepository(query, page)) }
                .mapNotNull { it.data }
                .flowOn(Dispatchers.IO)
    }
    
    override suspend fun searchRepositoryPaging(query: String, page: Int): ApiResponse<List<User>> {
        return apiService.searchRepository(query, page)
    }
    
    override fun saveUserToLocal(user: User) {
        val data = gson.toJson(user)
        sharedPrefsApi.put(SharedPrefsKey.KEY_TOKEN, data)
    }
    
    override fun getUserFromLocal(): User? {
        return sharedPrefsApi.get(SharedPrefsKey.KEY_USER, User::class.java)
    }
}
