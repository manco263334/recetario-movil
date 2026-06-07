package com.dmm.recetario.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dmm.recetario.data.local.database.dao.CategoryDaoImpl
import com.dmm.recetario.data.local.database.dao.RecipeDaoImpl
import com.dmm.recetario.data.local.database.dao.UserDaoImpl
import com.dmm.recetario.data.local.database.entity.CategoryEntityImpl
import com.dmm.recetario.data.local.database.entity.RecipeCategoryCrossRefImpl
import com.dmm.recetario.data.local.database.entity.RecipeEntityImpl
import com.dmm.recetario.data.local.database.entity.TokenUserRefImpl
import com.dmm.recetario.data.local.database.entity.UserEntityImpl

@Database (
    entities = [
        UserEntityImpl::class,
        CategoryEntityImpl::class,
        RecipeEntityImpl::class,
        RecipeCategoryCrossRefImpl::class,
        TokenUserRefImpl::class
    ],
    version = 6,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun recipeDao(): RecipeDaoImpl

    abstract fun categoryDao(): CategoryDaoImpl

    abstract fun userDao(): UserDaoImpl
}