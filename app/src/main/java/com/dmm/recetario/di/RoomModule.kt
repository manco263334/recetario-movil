package com.dmm.recetario.di

import android.content.Context
import androidx.room.Room
import com.dmm.recetario.data.local.database.AppDatabase
import com.dmm.recetario.data.local.database.Converters
import com.dmm.recetario.domain.dao.CategoryDao
import com.dmm.recetario.domain.dao.RecipeDao
import com.dmm.recetario.domain.dao.UserDao
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
    fun provideUserDao(db: AppDatabase): UserDao {
        return db.userDao()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(db: AppDatabase): CategoryDao {
        return db.categoryDao()
    }

    @Provides
    @Singleton
    fun provideRecipeDao(db: AppDatabase): RecipeDao {
        return db.recipeDao()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }
}