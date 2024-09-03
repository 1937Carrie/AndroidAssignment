package com.dumchykov.socialnetworkdemo.data.di

import android.content.Context
import com.dumchykov.socialnetworkdemo.BuildConfig
import com.dumchykov.socialnetworkdemo.data.webapi.ContactRepositoryImpl
import com.dumchykov.socialnetworkdemo.domain.webapi.ContactApiService
import com.dumchykov.socialnetworkdemo.domain.webapi.ContactRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideWebApi(client: OkHttpClient): ContactApiService {
        return Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .build()
            .create()
    }

    @Provides
    fun provideContactRepository(contactApiService: ContactApiService): ContactRepository {
        return ContactRepositoryImpl(contactApiService)
    }
}