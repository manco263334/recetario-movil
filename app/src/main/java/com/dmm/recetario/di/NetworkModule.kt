package com.dmm.recetario.di

import com.dmm.recetario.data.remote.interceptor.AuthInterceptor
import com.dmm.recetario.data.remote.retrofit.AuthRemote
import com.dmm.recetario.data.remote.retrofit.CategoryRemote
import com.dmm.recetario.data.remote.retrofit.RecipeRemote
import com.dmm.recetario.data.remote.retrofit.UserRemote
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
//    private const val BASE_URL = "http://10.0.2.2:8080/api/"
    private const val BASE_URL = "http://192.168.11.125:8080/api/"

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthRemote(retrofit: Retrofit): AuthRemote {
        return retrofit.create(AuthRemote::class.java)
    }

    @Singleton
    @Provides
    fun provideUserRemote(retrofit: Retrofit): UserRemote {
        return retrofit.create(UserRemote::class.java)
    }

    @Singleton
    @Provides
    fun provideCategoryRemote(retrofit: Retrofit): CategoryRemote {
        return retrofit.create(CategoryRemote::class.java)
    }

    @Singleton
    @Provides
    fun provideRecipeRemote(retrofit: Retrofit): RecipeRemote {
        return retrofit.create(RecipeRemote::class.java)
    }
}