package com.dmm.recetario.di

import android.content.Context
import com.dmm.recetario.core.utils.helper.ResourceHelper
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
        return TokenManagerImpl(context = context)
    }

    @Provides
    @Singleton
    fun provideUserManager (
        authService: AuthService,
        userService: UserService,
        tokenManager: TokenManager,
        userRepository: UserRepository,
        resourceHelper: ResourceHelper,
        userDao: UserDao<UserEntity, TokenUserRef, RecipeEntity>,
    ): UserManager {
        return UserManagerImpl (
            userDao = userDao,
            userService = userService,
            authService = authService,
            tokenManager = tokenManager,
            resourceHelper = resourceHelper,
            userRepository = userRepository
        )
    }
}