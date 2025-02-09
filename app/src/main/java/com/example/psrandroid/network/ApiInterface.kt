package com.example.psrandroid.network

import com.example.psrandroid.dto.ImageUpdate
import com.example.psrandroid.dto.UpdateLocation
import com.example.psrandroid.dto.UserCredential
import com.example.psrandroid.repository.HomeRepository
import com.example.psrandroid.repository.HomeRepository_Factory
import com.example.psrandroid.response.AuthResponse
import com.example.psrandroid.response.RateMetal
import com.example.psrandroid.response.RateScreenResponse
import com.example.psrandroid.response.DealerResponse
import com.example.psrandroid.response.LmeResponse
import com.example.psrandroid.response.LocationResponse
import com.example.psrandroid.response.PrimeUser
import com.example.psrandroid.response.SearchSubMetal
import com.example.psrandroid.ui.screen.adPost.models.AllAds
import com.example.psrandroid.ui.screen.adPost.models.CreatePost
import com.example.psrandroid.ui.screen.adPost.models.ResponseCreatePost
import com.example.psrandroid.ui.screen.home.model.HomeResponse
import com.example.psrandroid.ui.screen.rate.models.AllSubMetalData
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
    ): RateScreenResponse

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

    @GET("getPremiumUsers")
    suspend fun getPremiumUser(): PrimeUser

    @GET("searchSuggestion")
    suspend fun getSearchMetals(
        @Query("location_id") locationId: String,
        @Query("metal_name") metalName: String,
    ): RateMetal

    @GET("search")
    suspend fun getSubMetals(
        @Query("location_id") locationId: String,
        @Query("metal_name") metalName: String,
    ): SearchSubMetal

    @GET("LMEMetals")
    suspend fun getLMEMetals(
    ): LmeResponse

    @GET("Ads/getAds")
    suspend fun getAllAds(): AllAds

    @GET("Ads/getAdsByLocation")
    suspend fun getAdsByLocation(
        @Query("location") location: String,
    ): AllAds

    @GET("Ads/getAdsByUser")
    suspend fun getAdsByUser(
        @Query("user_id") location: String,
    ): AllAds

    @GET("Ads/getSubmetals")
    suspend fun getAllSubMetals(): AllSubMetalData

    @POST("Ads/create")
    suspend fun createPost(
        @Body createPost: CreatePost
    ): ResponseCreatePost

    @GET("HomeData")
    suspend fun getHomeData(): HomeResponse
}