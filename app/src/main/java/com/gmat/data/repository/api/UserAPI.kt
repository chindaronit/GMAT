package com.gmat.data.repository.api

import com.gmat.data.model.UserModel
import retrofit2.Response
import retrofit2.http.*

interface UserAPI {

    @POST("users")
    suspend fun addUser(
        @Body user: UserModel,
        @Header("Authorization") token: String
    ): Response<Unit>

    @GET("users/get/ph")
    suspend fun getUserByPhone(
        @Query("phNo") phoneNumber: String,
        @Header("Authorization") token: String
    ): Response<UserModel>


    @GET("users")
    suspend fun getUserByUserId(
        @Query("userId") userId: String,
        @Header("Authorization") token: String
    ): Response<UserModel>

    @POST("users/update")
    suspend fun updateUser(
        @Body user: UserModel,
        @Header("Authorization") token: String
    ): Response<Unit>
}
