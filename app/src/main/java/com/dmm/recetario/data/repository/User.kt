package com.dmm.recetario.data.repository

import com.dmm.recetario.core.utils.extension.isNotNull
import com.dmm.recetario.core.utils.handler.handleApiCall
import com.dmm.recetario.core.utils.helper.ResourceHelper
import com.dmm.recetario.core.utils.mapper.toDomain
import com.dmm.recetario.data.remote.retrofit.UserRemote
import com.dmm.recetario.domain.model.User
import com.dmm.recetario.domain.repository.UserRepository

class UserRepositoryImpl (
    private val remote: UserRemote,
    private val resourceHelper: ResourceHelper
) : UserRepository {
    override suspend fun getAllUsers (
        page: Int,
        size: Int,
        withRecipes: Boolean?
    ): List<User> {
        val users = handleApiCall(resourceHelper) {
            remote.getAllUsers (
                page = page,
                size = size,
                withRecipes = withRecipes
            )
        }

        return users?.content?.map { it.toDomain() } ?: emptyList()
    }

    override suspend fun getUser (
        id: String,
        withRecipes: Boolean?
    ): User {
        val user = handleApiCall(resourceHelper) {
            remote.getUser (
                id = id,
                withRecipes = withRecipes
            )
        }

        assert(user.isNotNull())

        return user!!.toDomain()
    }

    override suspend fun updateUser (
        id: String,
        data: User
    ): User {
        val user = handleApiCall(resourceHelper) { remote.updateUser(id, data) }

        assert(user.isNotNull())

        return user!!.toDomain()
    }

    override suspend fun deleteUser(id: String) {
        handleApiCall(resourceHelper) { remote.deleteUser(id) }
    }
}