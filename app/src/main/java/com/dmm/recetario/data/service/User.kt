package com.dmm.recetario.data.service

import android.util.Log
import com.dmm.recetario.core.utils.extension.isNotNull
import com.dmm.recetario.domain.exceptions.APIException
import com.dmm.recetario.core.utils.mapper.toDomain
import com.dmm.recetario.core.utils.mapper.toEntity
import com.dmm.recetario.domain.dao.UserDao
import com.dmm.recetario.domain.model.AnonymousUser
import com.dmm.recetario.domain.model.User
import com.dmm.recetario.domain.repository.UserRepository
import com.dmm.recetario.domain.service.UserService
import com.dmm.recetario.domain.use_cases.user.DeleteUserUseCase
import com.dmm.recetario.domain.use_cases.user.UpdateUserUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserServiceImpl (
    private val updateUserUseCase: UpdateUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val userRepository: UserRepository,
    private val dao: UserDao
): UserService {
    override fun getAllUsers(): Flow<List<User>> {
        return dao.getUsers().map { users ->
            users.map { user ->
                user.toDomain()
            }
        }
    }

    override suspend fun syncUsers (
        page: Int,
        size: Int,
        withRecipes: Boolean?
    ): Boolean {
        return try {
            val users = userRepository.getAllUsers (
                page = page,
                size = size,
                withRecipes = withRecipes
            )
            dao.saveUsers(users.map { it.toEntity() })

            true
        } catch (e: APIException) {
            Log.e("UserService", "Error syncing users: ${e.message}", e)
            false
        }
    }

    override fun getUserById(id: String): Flow<User?> {
        return dao.getUser(id).map { user ->
            user?.toDomain()
        }
    }

    override fun getUserByEmail(email: String): Flow<User?> {
        return dao.getUserByEmail(email).map { user ->
            user?.toDomain()
        }
    }

    override fun getUserByUsername(username: String): Flow<User?> {
        return dao.getUserByUsername(username).map { user ->
            user?.toDomain()
        }
    }

    override fun getUserByTokenOrAnonymous(token: String): Flow<User?> {
        return dao.getUserByToken(token).map { user ->
            user?.toDomain() ?: AnonymousUser()
        }
    }

    override suspend fun updateUser (
        id: String,
        data: User
    ): User {
        val user = updateUserUseCase(id, data)

        assert(user.isNotNull())

        return user!!
    }

    override suspend fun deleteUser(id: String) {
        assert(deleteUserUseCase(id))
    }
}