package com.example.flowmvvm.data.source.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.flowmvvm.data.model.User
import com.example.flowmvvm.data.source.local.dao.AppDatabase
import com.example.flowmvvm.data.source.local.dao.UserEntity
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface AppDBRepository {
    fun getUsers(): LiveData<MutableList<User>>
    
    fun deleteUser(user: User): Flow<Unit>
    
    fun insertUser(user: User): Flow<Unit>
    
    fun insertAllUser(users: List<User>): Flow<Unit>
}

class AppDBRepositoryImpl
constructor(private val appDB: AppDatabase, private val gson: Gson) : AppDBRepository {
    
    override fun getUsers(): LiveData<MutableList<User>> {
        return transformUserEntity(appDB.userDao().getAll())
    }
    
    override fun deleteUser(user: User): Flow<Unit> {
        return flow {
            val entity = UserEntity().userToEntity(user, gson)
            appDB.userDao().delete(entity)
            emit(Unit)
        }.flowOn(Dispatchers.IO)
    }
    
    override fun insertUser(user: User): Flow<Unit> {
        return flow {
            val entity = UserEntity().userToEntity(user, gson)
            appDB.userDao().insertOrUpdate(entity)
            emit(Unit)
        }.flowOn(Dispatchers.IO)
    }
    
    override fun insertAllUser(users: List<User>): Flow<Unit> {
        return flow {
            val entities = users.map { UserEntity().userToEntity(it, gson) }
            appDB.userDao().insertOrUpdateAll(entities)
            emit(Unit)
        }.flowOn(Dispatchers.IO)
    }
    
    private fun transformUserEntity(data: LiveData<List<UserEntity>>): LiveData<MutableList<User>> {
        return Transformations.map(data) { result ->
            val list = mutableListOf<User>()
            result?.forEach { entity ->
                list.add(entity.userFromEntity(gson))
            }
            list
        }
    }
}
