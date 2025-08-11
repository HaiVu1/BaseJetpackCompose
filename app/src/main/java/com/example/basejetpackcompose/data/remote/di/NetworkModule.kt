package com.example.basejetpackcompose.data.remote.di

import com.example.basejetpackcompose.base.Constant
import com.example.basejetpackcompose.data.remote.API
import com.example.basejetpackcompose.data.remote.service.PokemonServices
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(httpLoggingInterceptor)
        builder.connectTimeout(Constant.TIME_OUT, TimeUnit.SECONDS)
        builder.readTimeout(Constant.TIME_OUT, TimeUnit.SECONDS)
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        gson: Gson,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder().baseUrl(API.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient).build()
    }

    @Provides
    @Singleton
    fun providePokemonServices(retrofit: Retrofit): PokemonServices {
        return retrofit.create(PokemonServices::class.java)
    }
}