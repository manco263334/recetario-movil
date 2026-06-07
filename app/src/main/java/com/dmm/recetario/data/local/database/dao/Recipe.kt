package com.dmm.recetario.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.dmm.recetario.data.local.database.entity.CategoryEntity
import com.dmm.recetario.data.local.database.entity.RecipeCategoryCrossRef
import com.dmm.recetario.data.local.database.entity.RecipeEntity
import com.dmm.recetario.data.local.database.entity.UserEntity
import com.dmm.recetario.domain.dao.RecipeDao
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDaoImpl: RecipeDao {
    @Transaction
    @Query("SELECT * FROM recipes")
    override fun getRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    override fun getRecipe(id: String): Flow<RecipeEntity?>

    @Upsert
    override suspend fun saveRecipes(recipes: List<RecipeEntity>)

    @Upsert
    override suspend fun saveRecipe(recipe: RecipeEntity)

    @Upsert
    override suspend fun insertReferences(refs: List<RecipeCategoryCrossRef>)

    @Upsert
    override suspend fun insertReference(ref: RecipeCategoryCrossRef)

    @Query("DELETE FROM recipes")
    override suspend fun clear()

    @Query("DELETE FROM recipes WHERE id = :id")
    override suspend fun deleteRecipe(id: String)

    @Transaction
    @Query ("""
        SELECT u.* FROM users AS u
        INNER JOIN recipes AS r ON r.user_id = u.id
        WHERE r.id = :recipeId
        LIMIT 1
    """)
    override fun getUser(recipeId: String): Flow<UserEntity?>

    @Transaction
    @Query ("""
        SELECT c.* FROM categories_recipes
        INNER JOIN categories AS c on c.id = category_id
        WHERE recipe_id = :recipeId
    """)
    override fun getCategories(recipeId: String): Flow<List<CategoryEntity>>
}