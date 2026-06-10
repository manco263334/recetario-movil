package com.dmm.recetario.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.dmm.recetario.data.local.database.entity.CategoryEntityImpl
import com.dmm.recetario.data.local.database.entity.RecipeCategoryCrossRefImpl
import com.dmm.recetario.data.local.database.entity.RecipeEntityImpl
import com.dmm.recetario.data.local.database.entity.UserEntityImpl
import com.dmm.recetario.domain.dao.RecipeDao
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDaoImpl : RecipeDao <
    RecipeEntityImpl,
    RecipeCategoryCrossRefImpl,
    UserEntityImpl,
    CategoryEntityImpl
> {
    @Transaction
    @Query("SELECT * FROM recipes")
    override fun getRecipes(): Flow<List<RecipeEntityImpl>>

    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    override fun getRecipe(id: String): Flow<RecipeEntityImpl?>

    @Upsert
    override suspend fun saveRecipes(recipes: List<RecipeEntityImpl>)

    @Upsert
    override suspend fun saveRecipe(recipe: RecipeEntityImpl)

    @Upsert
    override suspend fun insertReferences(refs: List<RecipeCategoryCrossRefImpl>)

    @Upsert
    override suspend fun insertReference(ref: RecipeCategoryCrossRefImpl)

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
    override fun getUser(recipeId: String): Flow<UserEntityImpl?>

    @Transaction
    @Query ("""
        SELECT c.* FROM categories_recipes
        INNER JOIN categories AS c on c.id = category_id
        WHERE recipe_id = :recipeId
    """)
    override fun getCategories(recipeId: String): Flow<List<CategoryEntityImpl>>
}