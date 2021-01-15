package com.example.flowmvvm.data.source.repositories

import com.example.flowmvvm.data.model.User
import com.example.flowmvvm.data.source.local.sharedprf.SharedPrefsApi
import com.example.flowmvvm.data.source.local.sharedprf.SharedPrefsKey
import com.example.flowmvvm.data.source.remote.service.AppApi
import com.google.gson.Gson


interface UserRepository {

    fun saveUserToLocal(user: User)

    fun getUserFromLocal(): User?
}

class UserRepositoryImpl
constructor(private val api: AppApi, private val sharedPrefsApi: SharedPrefsApi) :
    UserRepository {

    private val gson = Gson()

    val user = User()

    override fun saveUserToLocal(user: User) {
        val data = gson.toJson(user)
        sharedPrefsApi.put(SharedPrefsKey.KEY_TOKEN, data)
    }

    override fun getUserFromLocal(): User? {
        return sharedPrefsApi.get(SharedPrefsKey.KEY_USER, User::class.java)
    }

}
