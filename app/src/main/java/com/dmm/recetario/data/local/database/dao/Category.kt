package com.dmm.recetario.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.dmm.recetario.data.local.database.entity.CategoryEntity
import com.dmm.recetario.data.local.database.entity.RecipeCategoryCrossRef
import com.dmm.recetario.data.local.database.entity.RecipeEntity
import com.dmm.recetario.domain.dao.CategoryDao
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDaoImpl: CategoryDao {
    @Query("SELECT * FROM categories")
    override fun getCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    override fun getCategory(id: String): Flow<CategoryEntity?>

    @Upsert
    override suspend fun saveCategories(categories: List<CategoryEntity>)

    @Upsert
    override suspend fun saveCategory(category: CategoryEntity)

    @Upsert
    override suspend fun insertReferences(refs: List<RecipeCategoryCrossRef>)

    @Upsert
    override suspend fun insertReference(ref: RecipeCategoryCrossRef)

    @Query("DELETE FROM categories")
    override suspend fun clear()

    @Query("DELETE FROM categories WHERE id = :id")
    override suspend fun deleteCategory(id: String)

    @Transaction
    @Query ("""
        SELECT r.* FROM categories_recipes 
        INNER JOIN recipes AS r ON r.id = recipe_id
        WHERE category_id = :categoryId
    """)
    override fun getRecipes(categoryId: String): Flow<List<RecipeEntity>>
}