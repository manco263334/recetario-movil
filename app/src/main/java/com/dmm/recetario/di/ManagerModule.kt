package com.dmm.recetario.di

import android.content.Context
import com.dmm.recetario.data.local.TokenManagerImpl
import com.dmm.recetario.data.local.UserManagerImpl
import com.dmm.recetario.domain.dao.UserDao
import com.dmm.recetario.domain.entity.RecipeEntity
import com.dmm.recetario.domain.entity.TokenUserRef
import com.dmm.recetario.domain.entity.UserEntity
import com.dmm.recetario.domain.manager.TokenManager
import com.dmm.recetario.domain.manager.UserManager
import com.dmm.recetario.domain.repository.UserRepository
import com.dmm.recetario.domain.service.AuthService
import com.dmm.recetario.domain.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ManagerModule {
    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManagerImpl(context)
    }

    @Provides
    @Singleton
    fun provideUserManager (
        tokenManager: TokenManager,
        authService: AuthService,
        userRepository: UserRepository,
        userDao: UserDao<UserEntity, TokenUserRef, RecipeEntity>,
        userService: UserService
    ): UserManager {
        return UserManagerImpl (
            tokenManager,
            authService,
            userRepository,
            userDao,
            userService
        )
    }
}