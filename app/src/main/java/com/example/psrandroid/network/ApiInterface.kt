package com.example.psrandroid.network

import com.example.psrandroid.dto.UserCredential
import com.example.psrandroid.response.DashboardResponse
import com.example.psrandroid.response.DealerResponse
import com.example.psrandroid.response.LocationResponse
import com.example.psrandroid.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @POST("user/login")
    suspend fun userLogin(
        @Body userCredential: UserCredential
    ): LoginResponse

    @POST("user/register")
    suspend fun register(
        @Body userCredential: UserCredential
    ): LoginResponse

    @GET("locations")
    suspend fun getLocation(): LocationResponse

    @GET("dealers")
    suspend fun getDealerList(): DealerResponse

    @GET("general-rate-sheet")
    suspend fun getDashboardData(
        @Query("location_id") locationId: String,
        @Query("date") date: String
    ): DashboardResponse
}