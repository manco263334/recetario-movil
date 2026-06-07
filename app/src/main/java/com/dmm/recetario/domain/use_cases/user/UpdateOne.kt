package com.dmm.recetario.domain.use_cases.user

import android.util.Log
import com.dmm.recetario.core.utils.extension.isNotNull
import com.dmm.recetario.domain.exception.APIException
import com.dmm.recetario.core.utils.mapper.toEntity
import com.dmm.recetario.domain.dao.UserDao
import com.dmm.recetario.domain.entity.RecipeEntity
import com.dmm.recetario.domain.entity.TokenUserRef
import com.dmm.recetario.domain.entity.UserEntity
import com.dmm.recetario.domain.model.User
import com.dmm.recetario.domain.repository.UserRepository
import jakarta.inject.Inject

class UpdateUserUseCase @Inject constructor (
    private val repository: UserRepository,
    private val dao: UserDao<UserEntity, TokenUserRef, RecipeEntity>
) {
    suspend operator fun invoke(id: String, data: User): User? {
        var user: User?

        try {
            user = repository.updateUser(id, data)
        } catch (e: APIException) {
            Log.w("UpdateUserUseCase", "${e.message}")
            return null
        }

        if (user.isNotNull()) {
            dao.saveUsers(listOf(user.toEntity()))
        }

        return user
    }
}