package com.dmm.recetario.domain.use_cases.user

import android.util.Log
import com.dmm.recetario.domain.dao.UserDao
import com.dmm.recetario.domain.entity.RecipeEntity
import com.dmm.recetario.domain.entity.TokenUserRef
import com.dmm.recetario.domain.entity.UserEntity
import com.dmm.recetario.domain.exception.APIException
import com.dmm.recetario.domain.repository.UserRepository
import jakarta.inject.Inject

class DeleteUserUseCase @Inject constructor (
    private val repository: UserRepository,
    private val dao: UserDao<UserEntity, TokenUserRef, RecipeEntity>
) {
    suspend operator fun invoke(id: String): Boolean {
        try {
            repository.deleteUser(id)
        } catch (e: APIException) {
            Log.w("DeleteUserUseCase", "${e.message}")
            return false
        }

        dao.deleteUser(id)

        return true
    }
}