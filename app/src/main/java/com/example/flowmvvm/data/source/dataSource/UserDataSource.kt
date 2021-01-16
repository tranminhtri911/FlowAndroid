package com.example.flowmvvm.data.source.dataSource

import com.example.flowmvvm.data.model.User
import com.example.flowmvvm.data.source.repositories.UserRepository

class UserDataSource
constructor(
        private val query: String,
        private val userRepository: UserRepository,
) : BaseDataSource<User>() {
    
    override suspend fun loadData(nextPage: Int): List<User> {
        val response = userRepository.searchRepositoryPaging(query, nextPage)
        return response.data ?: emptyList()
    }
    
}