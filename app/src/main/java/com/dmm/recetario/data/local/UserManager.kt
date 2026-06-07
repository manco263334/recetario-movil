package com.dmm.recetario.data.local

import android.util.Log
import com.dmm.recetario.core.jwt.isTokenExpired
import com.dmm.recetario.domain.exceptions.APIException
import com.dmm.recetario.core.utils.mapper.toEntity
import com.dmm.recetario.domain.dao.UserDao
import com.dmm.recetario.domain.model.AnonymousUser
import com.dmm.recetario.domain.model.User
import com.dmm.recetario.domain.repository.UserRepository
import com.dmm.recetario.domain.service.AuthService
import com.dmm.recetario.domain.service.UserService
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf

@Singleton
class UserManager @Inject constructor (
    private val tokenManager: TokenManager,
    private val authService: AuthService,
    private val userRepository: UserRepository,
    private val userDao: UserDao,
    private val userService: UserService
) {
    suspend fun getUserByAPI(): User {
        val me = authService.me()
        val user = userRepository.getUser(me.id, false)

        return user
    }

    suspend fun syncUser() {
        val token = tokenManager.token.firstOrNull()?.ifBlank { null }

        if (token == null) return

        if (isTokenExpired(token)) return

        try {
            val userFromAPI = getUserByAPI()

            userDao.saveUser(userFromAPI.toEntity())
        } catch (e: APIException) {
            Log.e("UserManager", "Error syncing user: ${e.message}", e)
        }
    }

    fun getUserLocal(token: String?): Flow<User?> {
        return if (token == null) {
            flowOf(null)
        } else if (token.isBlank()) {
            flowOf(AnonymousUser())
        } else {
            userService.getUserByTokenOrAnonymous(token)
        }
    }
}