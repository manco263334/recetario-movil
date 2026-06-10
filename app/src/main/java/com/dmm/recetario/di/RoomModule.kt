package com.dmm.recetario.di

import android.content.Context
import androidx.room.Room
import com.dmm.recetario.data.local.database.AppDatabase
import com.dmm.recetario.data.local.database.Converters
import com.dmm.recetario.domain.dao.CategoryDao
import com.dmm.recetario.domain.dao.RecipeDao
import com.dmm.recetario.domain.dao.UserDao
import com.dmm.recetario.domain.entity.CategoryEntity
import com.dmm.recetario.domain.entity.RecipeCategoryCrossRef
import com.dmm.recetario.domain.entity.RecipeEntity
import com.dmm.recetario.domain.entity.TokenUserRef
import com.dmm.recetario.domain.entity.UserEntity
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    private const val APP_DATABASE_NAME = "recetario.db"

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context, gson: Gson): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, APP_DATABASE_NAME)
            .addTypeConverter(Converters(gson))
            .build()
    }

    @Provides
    @Singleton
    @Suppress("unchecked_cast")
    fun provideUserDao(db: AppDatabase): UserDao<UserEntity, TokenUserRef,
            RecipeEntity> {
        return db.userDao() as UserDao<UserEntity, TokenUserRef,
                RecipeEntity>
    }

    @Provides
    @Singleton
    @Suppress("unchecked_cast")
    fun provideCategoryDao(db: AppDatabase): CategoryDao<CategoryEntity,
            RecipeCategoryCrossRef, RecipeEntity> {
        return db.categoryDao() as CategoryDao<CategoryEntity,
                RecipeCategoryCrossRef, RecipeEntity>
    }

    @Provides
    @Singleton
    @Suppress("unchecked_cast")
    fun provideRecipeDao(db: AppDatabase): RecipeDao<RecipeEntity, RecipeCategoryCrossRef,
            UserEntity, CategoryEntity> {
        return db.recipeDao() as RecipeDao<RecipeEntity, RecipeCategoryCrossRef,
                UserEntity, CategoryEntity>
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }
}