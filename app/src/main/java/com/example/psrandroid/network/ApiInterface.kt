package com.example.psrandroid.network

import com.example.psrandroid.dto.UserCredential
import com.example.psrandroid.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST("user/login")
    suspend fun userLogin(
        @Body userCredential: UserCredential
    ): LoginResponse

    @POST("user/register")
    suspend fun register(
        @Body userCredential: UserCredential
    ): LoginResponse
}