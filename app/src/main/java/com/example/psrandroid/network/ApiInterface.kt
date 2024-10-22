package com.example.psrandroid.network

import com.example.psrandroid.dto.ImageUpdate
import com.example.psrandroid.dto.UserCredential
import com.example.psrandroid.dto.UpdateLocation
import com.example.psrandroid.response.AuthResponse
import com.example.psrandroid.response.DashboardResponse
import com.example.psrandroid.response.DealerResponse
import com.example.psrandroid.response.LocationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {

    @POST("user/login")
    suspend fun userLogin(
        @Body userCredential: UserCredential
    ): AuthResponse

    @POST("user/register")
    suspend fun register(
        @Body userCredential: UserCredential
    ): AuthResponse

    @GET("locations")
    suspend fun getLocation(): LocationResponse

    @GET("dealers")
    suspend fun getDealerList(): DealerResponse

    @GET("metals/location")
    suspend fun getDashboardData(
        @Query("location_id") locationId: String,
        @Query("date") date: String
    ): DashboardResponse

    @POST("updateLocation")
    suspend fun updateUserLocation(
        @Body updateLocation: UpdateLocation
    ): AuthResponse

    @POST("updateUserImage")
    suspend fun updateUserImage(
        @Body imageUpdate: ImageUpdate
    ): AuthResponse

    @POST("updateUser")
    suspend fun updateUserData(
        @Body userCredential: UserCredential
    ): AuthResponse

}