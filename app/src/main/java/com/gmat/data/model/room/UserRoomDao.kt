package com.gmat.data.model.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserRoomDao {
    // Upsert is short for insert + update
    @Upsert
    suspend fun upsertUser(userRoomModel: UserRoomModel)

    // Flow will notify whenever there are changes in the table
    // Query to get the stored user
    @Query("SELECT * FROM UserRoomModel LIMIT 1")
    fun getUser(): Flow<UserRoomModel?>


    @Delete
    suspend fun deleteUser(userRoomModel: UserRoomModel)
}
