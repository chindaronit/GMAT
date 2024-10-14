package com.gmat.data.model.room

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [UserRoomModel::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract val dao: UserRoomDao  // Ensure this method is defined
}