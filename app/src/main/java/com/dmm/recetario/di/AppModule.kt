package com.dmm.recetario.di

import com.dmm.recetario.data.remote.retrofit.AuthRemote
import com.dmm.recetario.data.remote.retrofit.CategoryRemote
import com.dmm.recetario.data.remote.retrofit.RecipeRemote
import com.dmm.recetario.data.remote.retrofit.UserRemote
import com.dmm.recetario.data.repository.AuthRepositoryImpl
import com.dmm.recetario.data.repository.CategoryRepositoryImpl
import com.dmm.recetario.data.repository.RecipeRepositoryImpl
import com.dmm.recetario.data.repository.UserRepositoryImpl
import com.dmm.recetario.data.service.AuthServiceImpl
import com.dmm.recetario.data.service.CategoryServiceImpl
import com.dmm.recetario.data.service.RecipeServiceImpl
import com.dmm.recetario.data.service.UserServiceImpl
import com.dmm.recetario.domain.dao.CategoryDao
import com.dmm.recetario.domain.dao.RecipeDao
import com.dmm.recetario.domain.dao.UserDao
import com.dmm.recetario.domain.repository.AuthRepository
import com.dmm.recetario.domain.repository.CategoryRepository
import com.dmm.recetario.domain.repository.RecipeRepository
import com.dmm.recetario.domain.repository.UserRepository
import com.dmm.recetario.domain.service.AuthService
import com.dmm.recetario.domain.service.CategoryService
import com.dmm.recetario.domain.service.RecipeService
import com.dmm.recetario.domain.service.UserService
import com.dmm.recetario.domain.use_cases.category.CreateCategoryUseCase
import com.dmm.recetario.domain.use_cases.category.DeleteCategoryUseCase
import com.dmm.recetario.domain.use_cases.category.UpdateCategoryUseCase
import com.dmm.recetario.domain.use_cases.recipe.CreateRecipeUseCase
import com.dmm.recetario.domain.use_cases.recipe.DeleteRecipeUseCase
import com.dmm.recetario.domain.use_cases.recipe.UpdateRecipeUseCase
import com.dmm.recetario.domain.use_cases.user.DeleteUserUseCase
import com.dmm.recetario.domain.use_cases.user.UpdateUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAuthRepository(remote: AuthRemote): AuthRepository {
        return AuthRepositoryImpl(remote)
    }

    @Provides
    @Singleton
    fun provideAuthService(repository: AuthRepository): AuthService {
        return AuthServiceImpl(repository)
    }

    @Provides
    @Singleton
    fun provideRecipeRepository(remote: RecipeRemote): RecipeRepository {
        return RecipeRepositoryImpl(remote)
    }

    @Provides
    @Singleton
    fun provideRecipeService (
        createRecipeUseCase: CreateRecipeUseCase,
        updateRecipeUSECase: UpdateRecipeUseCase,
        deleteRecipeUseCase: DeleteRecipeUseCase,
        repository: RecipeRepository,
        dao: RecipeDao
    ): RecipeService {
        return RecipeServiceImpl(
            createRecipeUseCase,
            updateRecipeUSECase,
            deleteRecipeUseCase,
            repository,
            dao
        )
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(remote: CategoryRemote): CategoryRepository {
        return CategoryRepositoryImpl(remote)
    }

    @Provides
    @Singleton
    fun provideCategoryService (
        createCategoryUseCase: CreateCategoryUseCase,
        updateCategoryUseCase: UpdateCategoryUseCase,
        deleteCategoryService: DeleteCategoryUseCase,
        repository: CategoryRepository,
        dao: CategoryDao
    ): CategoryService {
        return CategoryServiceImpl(
            createCategoryUseCase,
            updateCategoryUseCase,
            deleteCategoryService,
            repository,
            dao
        )
    }

    @Provides
    @Singleton
    fun provideUserRepository(remote: UserRemote): UserRepository {
        return UserRepositoryImpl(remote)
    }

    @Provides
    @Singleton
    fun provideUserService (
        updateUserUseCase: UpdateUserUseCase,
        deleteUserUseCase: DeleteUserUseCase,
        repository: UserRepository,
        dao: UserDao
    ): UserService {
        return UserServiceImpl(
            updateUserUseCase,
            deleteUserUseCase,
            repository,
            dao
        )
    }
}