package com.gmat.di

import com.gmat.data.repository.api.LeaderboardAPI
import com.gmat.data.repository.api.TransactionAPI
import com.gmat.data.repository.api.UserAPI
import com.gmat.env.CONSTANTS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            // You can add interceptors, timeouts, or other configurations here
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(CONSTANTS.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideUserAPI(retrofit: Retrofit): UserAPI {
        return retrofit.create(UserAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideLeaderboardAPI(retrofit: Retrofit): LeaderboardAPI {
        return retrofit.create(LeaderboardAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideTransactionAPI(retrofit: Retrofit): TransactionAPI {
        return retrofit.create(TransactionAPI::class.java)
    }
}