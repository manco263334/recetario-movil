package com.dmm.recetario.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dmm.recetario.data.local.database.dao.CategoryDaoImpl
import com.dmm.recetario.data.local.database.dao.RecipeDaoImpl
import com.dmm.recetario.data.local.database.dao.UserDaoImpl
import com.dmm.recetario.data.local.database.entity.CategoryEntity
import com.dmm.recetario.data.local.database.entity.RecipeCategoryCrossRef
import com.dmm.recetario.data.local.database.entity.RecipeEntity
import com.dmm.recetario.data.local.database.entity.TokenUserRef
import com.dmm.recetario.data.local.database.entity.UserEntity

@Database (
    entities = [
        UserEntity::class,
        CategoryEntity::class,
        RecipeEntity::class,
        RecipeCategoryCrossRef::class,
        TokenUserRef::class
    ],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun recipeDao(): RecipeDaoImpl

    abstract fun categoryDao(): CategoryDaoImpl

    abstract fun userDao(): UserDaoImpl
}