package com.dmm.recetario.domain.dao

import com.dmm.recetario.domain.entity.RecipeEntity
import com.dmm.recetario.domain.entity.TokenUserRef
import com.dmm.recetario.domain.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserDao <
    IUserEntity : UserEntity,
    ITokenUserRef : TokenUserRef,
    IRecipeEntity : RecipeEntity
> {
    fun getUsers(): Flow<List<IUserEntity>>

    fun getUser(id: String): Flow<IUserEntity?>

    fun getUserByEmail(email: String): Flow<IUserEntity?>

    fun getUserByUsername(username: String): Flow<IUserEntity?>

    fun getUserByToken(token: String): Flow<IUserEntity?>

    suspend fun insertTokenRefs(refs: List<ITokenUserRef>)

    suspend fun insertTokenRef(ref: ITokenUserRef)

    suspend fun saveUsers(users: List<IUserEntity>)

    suspend fun saveUser(user: IUserEntity)

    suspend fun clear()

    suspend fun clearTokenRefs()

    suspend fun deleteUser(id: String)

    fun getRecipes(userId: String): Flow<List<IRecipeEntity>>
}