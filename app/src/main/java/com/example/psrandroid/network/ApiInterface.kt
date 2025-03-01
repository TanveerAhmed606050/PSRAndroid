package com.example.psrandroid.network

import com.example.psrandroid.dto.ImageUpdate
import com.example.psrandroid.dto.UserCredential
import com.example.psrandroid.response.LmeResponse
import com.example.psrandroid.response.LocationResponse
import com.example.psrandroid.response.PrimeUser
import com.example.psrandroid.ui.screen.adPost.models.MyAds
import com.example.psrandroid.ui.screen.adPost.models.ResponseCreatePost
import com.example.psrandroid.ui.screen.auth.models.AuthResponse
import com.example.psrandroid.ui.screen.auth.models.InfoDataResponse
import com.example.psrandroid.ui.screen.home.model.HomeResponse
import com.example.psrandroid.ui.screen.profile.models.UpdateUserData
import com.example.psrandroid.ui.screen.rate.models.MetalRateResponse
import com.example.psrandroid.ui.screen.rate.models.RateSuggestionResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @POST("updateUserImage")
    suspend fun updateUserImage(
        @Body imageUpdate: ImageUpdate
    ): AuthResponse

    @POST("updatePassword")
    suspend fun updatePassword(
        @Body updateUserData: UpdateUserData
    ): InfoDataResponse

    @POST("forgetPassword")
    suspend fun resetPassword(
        @Body updateUserData: UpdateUserData
    ): InfoDataResponse

    @POST("phoneValidate")
    suspend fun phoneValidate(
        @Body updateUserData: UpdateUserData
    ): InfoDataResponse

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
    ): RateSuggestionResponse

    @GET("search")
    suspend fun getSubMetals(
        @Query("location_id") locationId: String,
        @Query("metal_name") metalName: String,
    ): MetalRateResponse

    @GET("LMEMetals")
    suspend fun getLMEMetals(
    ): LmeResponse

    @GET("Ads/getAds")
    suspend fun getAllAds(
        @Query("city") city: String,
        @Query("metal_name") metalName: String,
        @Query("per_page") perPage: String,
        @Query("page") page: String,
    ): MyAds

    @GET("Ads/user")
    suspend fun getAdsByUser(
        @Query("user_id") userId: String,
        @Query("per_page") perPage: String,
        @Query("page") page: String,
    ): MyAds

    @Multipart
    @POST("Ads/create")
    suspend fun createPost(
        @Part("user_id") userId: RequestBody,
        @Part("metal_name") metalName: RequestBody,
        @Part("phone_number") phoneNumber: RequestBody,
        @Part("submetal") submetal: RequestBody,
        @Part("city") city: RequestBody,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part photos: List<MultipartBody.Part> // Use @Part for the list of photos
    ): ResponseCreatePost

    @GET("HomeData")
    suspend fun getHomeData(): HomeResponse
}