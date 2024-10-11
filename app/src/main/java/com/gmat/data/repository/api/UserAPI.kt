package com.gmat.data.repository.api

import com.gmat.data.model.UserModel
import retrofit2.Response
import retrofit2.http.*

interface UserAPI {
    @POST("/dev/users")
    suspend fun addUser(@Body user: UserModel): Response<Unit>

    // Pass phone number as a query parameter
    @GET("/dev/users/get/ph")
    suspend fun getUserByPhone(@Query("phNo") phoneNumber: String): Response<UserModel>

    // Pass VPA as a query parameter
    @GET("/dev/users/get/vpa")
    suspend fun getUserByVPA(@Query("vpa") vpa: String): Response<UserModel>

    // Pass userId as a query parameter
    @GET("/dev/users")
    suspend fun getUserByUserId(@Query("userId") userId: String): Response<UserModel>

    @POST("/dev/users/update")
    suspend fun updateUser(
        @Body user: UserModel
    ): Response<Unit>
}
