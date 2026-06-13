package com.dmm.recetario.data.local

import android.util.Log
import com.dmm.recetario.R
import com.dmm.recetario.core.jwt.isTokenExpired
import com.dmm.recetario.core.utils.helper.ResourceHelper
import com.dmm.recetario.domain.exception.APIException
import com.dmm.recetario.core.utils.mapper.toEntity
import com.dmm.recetario.domain.dao.UserDao
import com.dmm.recetario.domain.entity.RecipeEntity
import com.dmm.recetario.domain.entity.TokenUserRef
import com.dmm.recetario.domain.entity.UserEntity
import com.dmm.recetario.domain.manager.TokenManager
import com.dmm.recetario.domain.manager.UserManager
import com.dmm.recetario.domain.model.AnonymousUser
import com.dmm.recetario.domain.model.User
import com.dmm.recetario.domain.repository.UserRepository
import com.dmm.recetario.domain.service.AuthService
import com.dmm.recetario.domain.service.UserService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf

class UserManagerImpl (
    private val authService: AuthService,
    private val userService: UserService,
    private val tokenManager: TokenManager,
    private val userRepository: UserRepository,
    private val resourceHelper: ResourceHelper,
    private val userDao: UserDao<UserEntity, TokenUserRef, RecipeEntity>
) : UserManager {
    private suspend fun getUserByAPI(): User {
        val me = authService.me()
        val user = userRepository.getUser(me.id, false)

        return user
    }

    override suspend fun syncUser() {
        val token = tokenManager.token.firstOrNull()?.ifBlank { null }

        if (token == null) return

        if (isTokenExpired(token)) return

        try {
            val userFromAPI = getUserByAPI()

            userDao.saveUser(userFromAPI.toEntity())
        } catch (e: APIException) {
            val message = resourceHelper.getString(R.string.error_syncing_user)
            Log.e("UserManager", "$message: ${e.message}", e)
        }
    }

    override fun getUserLocal(token: String?): Flow<User?> {
        return if (token == null) {
            flowOf(null)
        } else if (token.isBlank()) {
            flowOf(AnonymousUser())
        } else {
            userService.getUserByTokenOrAnonymous(token)
        }
    }
}