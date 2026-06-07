package com.dmm.recetario.domain.dao

import com.dmm.recetario.data.local.database.entity.RecipeEntity
import com.dmm.recetario.data.local.database.entity.TokenUserRef
import com.dmm.recetario.data.local.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserDao {
    fun getUsers(): Flow<List<UserEntity>>

    fun getUser(id: String): Flow<UserEntity?>

    fun getUserByEmail(email: String): Flow<UserEntity?>

    fun getUserByUsername(username: String): Flow<UserEntity?>

    fun getUserByToken(token: String): Flow<UserEntity?>

    suspend fun insertTokenRefs(refs: List<TokenUserRef>)

    suspend fun insertTokenRef(ref: TokenUserRef)

    suspend fun saveUsers(users: List<UserEntity>)

    suspend fun saveUser(user: UserEntity)

    suspend fun clear()

    suspend fun clearTokenRefs()

    suspend fun deleteUser(id: String)

    fun getRecipes(userId: String): Flow<List<RecipeEntity>>
}