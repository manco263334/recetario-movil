package com.dmm.recetario.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.dmm.recetario.data.local.database.entity.RecipeEntityImpl
import com.dmm.recetario.data.local.database.entity.TokenUserRefImpl
import com.dmm.recetario.data.local.database.entity.UserEntityImpl
import com.dmm.recetario.domain.dao.UserDao
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDaoImpl: UserDao <
    UserEntityImpl,
    TokenUserRefImpl,
    RecipeEntityImpl
> {
    @Query("SELECT * FROM users")
    override fun getUsers(): Flow<List<UserEntityImpl>>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    override fun getUser(id: String): Flow<UserEntityImpl?>

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    override fun getUserByEmail(email: String): Flow<UserEntityImpl?>

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    override fun getUserByUsername(username: String): Flow<UserEntityImpl?>

    @Query ("""
        SELECT u.* FROM tokens_users AS t_u
        INNER JOIN users AS u ON u.email = t_u.email
        WHERE token = :token
        LIMIT 1
    """)
    override fun getUserByToken(token: String): Flow<UserEntityImpl?>

    @Upsert
    override suspend fun insertTokenRefs(refs: List<TokenUserRefImpl>)

    @Upsert
    override suspend fun insertTokenRef(ref: TokenUserRefImpl)

    @Upsert
    override suspend fun saveUsers(users: List<UserEntityImpl>)

    @Upsert
    override suspend fun saveUser(user: UserEntityImpl)

    @Query("DELETE FROM users")
    override suspend fun clear()

    @Query("DELETE FROM tokens_users")
    override suspend fun clearTokenRefs()

    @Query("DELETE FROM users WHERE id = :id")
    override suspend fun deleteUser(id: String)

    @Transaction
    @Query ("""
        SELECT * FROM recipes
        WHERE user_id = :userId
    """)
    override fun getRecipes(userId: String): Flow<List<RecipeEntityImpl>>
}