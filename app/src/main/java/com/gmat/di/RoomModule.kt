package com.gmat.di

import android.content.Context
import androidx.room.Room
import com.gmat.data.model.room.UserDatabase
import com.gmat.data.model.room.UserRoomDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideUserDatabase(
        @ApplicationContext app: Context
    ): UserDatabase = Room.databaseBuilder(
        app,
        UserDatabase::class.java,
        "UserDatabase"
    ).build()

    @Singleton
    @Provides
    fun provideUserRoomDao(db: UserDatabase): UserRoomDao = db.dao
}

