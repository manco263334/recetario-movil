package com.dmm.recetario.di

import com.dmm.recetario.core.utils.helper.ResourceHelper
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
import com.dmm.recetario.domain.entity.CategoryEntity
import com.dmm.recetario.domain.entity.RecipeCategoryCrossRef
import com.dmm.recetario.domain.entity.RecipeEntity
import com.dmm.recetario.domain.entity.TokenUserRef
import com.dmm.recetario.domain.entity.UserEntity
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
    fun provideAuthRepository (
        remote: AuthRemote,
        resourceHelper: ResourceHelper
    ): AuthRepository {
        return AuthRepositoryImpl(remote = remote, resourceHelper = resourceHelper)
    }

    @Provides
    @Singleton
    fun provideAuthService(repository: AuthRepository): AuthService {
        return AuthServiceImpl(repository = repository)
    }

    @Provides
    @Singleton
    fun provideRecipeRepository (
        remote: RecipeRemote,
        resourceHelper: ResourceHelper
    ): RecipeRepository {
        return RecipeRepositoryImpl(remote = remote, resourceHelper = resourceHelper)
    }

    @Provides
    @Singleton
    fun provideRecipeService (
        repository: RecipeRepository,
        resourceHelper: ResourceHelper,
        createRecipeUseCase: CreateRecipeUseCase,
        updateRecipeUseCase: UpdateRecipeUseCase,
        deleteRecipeUseCase: DeleteRecipeUseCase,
        dao: RecipeDao<RecipeEntity, RecipeCategoryCrossRef, UserEntity, CategoryEntity>
    ): RecipeService {
        return RecipeServiceImpl (
            dao = dao,
            repository = repository,
            resourceHelper = resourceHelper,
            createRecipeUseCase = createRecipeUseCase,
            updateRecipeUseCase = updateRecipeUseCase,
            deleteRecipeUseCase = deleteRecipeUseCase
        )
    }

    @Provides
    @Singleton
    fun provideCategoryRepository (
        remote: CategoryRemote,
        resourceHelper: ResourceHelper
    ): CategoryRepository {
        return CategoryRepositoryImpl(remote = remote, resourceHelper)
    }

    @Provides
    @Singleton
    fun provideCategoryService (
        repository: CategoryRepository,
        resourceHelper: ResourceHelper,
        createCategoryUseCase: CreateCategoryUseCase,
        updateCategoryUseCase: UpdateCategoryUseCase,
        deleteCategoryUseCase: DeleteCategoryUseCase,
        dao: CategoryDao<CategoryEntity, RecipeCategoryCrossRef, RecipeEntity>
    ): CategoryService {
        return CategoryServiceImpl (
            dao = dao,
            repository = repository,
            resourceHelper = resourceHelper,
            createCategoryUseCase = createCategoryUseCase,
            updateCategoryUseCase = updateCategoryUseCase,
            deleteCategoryUseCase = deleteCategoryUseCase
        )
    }

    @Provides
    @Singleton
    fun provideUserRepository (
        remote: UserRemote,
        resourceHelper: ResourceHelper
    ): UserRepository {
        return UserRepositoryImpl(remote = remote, resourceHelper = resourceHelper)
    }

    @Provides
    @Singleton
    fun provideUserService (
        repository: UserRepository,
        resourceHelper: ResourceHelper,
        updateUserUseCase: UpdateUserUseCase,
        deleteUserUseCase: DeleteUserUseCase,
        dao: UserDao<UserEntity, TokenUserRef, RecipeEntity>
    ): UserService {
        return UserServiceImpl (
            dao = dao,
            userRepository = repository,
            resourceHelper = resourceHelper,
            updateUserUseCase = updateUserUseCase,
            deleteUserUseCase = deleteUserUseCase
        )
    }
}