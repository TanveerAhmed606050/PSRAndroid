package com.example.psrandroid.ui.screen.adPost

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.psrandroid.dto.AdPostDto
import com.example.psrandroid.network.ApiInterface
import com.example.psrandroid.repository.GenericPagingRepository
import com.example.psrandroid.repository.HomeRepository
import com.example.psrandroid.storage.UserPreferences
import com.example.psrandroid.ui.screen.adPost.models.AdsData
import com.example.psrandroid.ui.screen.adPost.models.ResponseCreatePost
import com.example.psrandroid.ui.screen.rate.models.AllSubMetalData
import com.example.psrandroid.ui.screen.rate.models.SubData
import com.example.psrandroid.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class AdPostVM @Inject constructor(
    private val homeRepository: HomeRepository,
    private val genericPagingRepository: GenericPagingRepository,
    val userPreferences: UserPreferences,
    val apiInterface: ApiInterface,
) : ViewModel() {
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf("")

    //    var allAdsData by mutableStateOf<MyAds?>(null)
//    var locationAds by mutableStateOf<MyAds?>(null)
    private var subMetalData by mutableStateOf<AllSubMetalData?>(null)
    var suggestSubMetals by mutableStateOf<List<SubData>?>(null)
    var adPostResponse by mutableStateOf<ResponseCreatePost?>(null)
    var allAdsData by mutableStateOf<Flow<PagingData<AdsData>>>(flowOf(PagingData.empty()))
    var locationAds by mutableStateOf<Flow<PagingData<AdsData>>>(flowOf(PagingData.empty()))

    fun getAdsByLocation(adPostDto: AdPostDto) = viewModelScope.launch {
//        isLoading = true
//        Log.d("lsdjg", "location: $locationName")
//        val result = homeRepository.getAdsByLocation(locationName)
//        Log.d("lsdjg", "result: $result")
//        isLoading = false
//        if (result is Result.Success) {
//            locationAds = result.data
//        } else if (result is Result.Failure) {
//            error = result.exception.message ?: "Failure"
//        }
        Log.d("lsdjg", "AdPost: $adPostDto")
        val response = genericPagingRepository.getPagingData(
            requestData = adPostDto,
            updateRequest = { request, page -> request.copy(page = page.toString()) },
            fetchData = { request ->
                apiInterface.getAllAds(
                    city = request.city,
                    perPage = request.perPage,
                    page = request.page
                ).data
            }
        )
        locationAds = response
    }

    fun getAdsByUserid(adPostDto: AdPostDto) = viewModelScope.launch {
//        isLoading = true
//        val result = homeRepository.getAdsByUser(userId)
//        isLoading = false
//        Log.d("lsdjg", "result: $result")
//        if (result is Result.Success) {
//            allAdsData = result.data
//        } else if (result is Result.Failure) {
//            error = result.exception.message ?: "Failure"
//        }
        val response = genericPagingRepository.getPagingData(
            requestData = adPostDto,
            updateRequest = { request, page -> request.copy(page = page.toString()) },
            fetchData = { request ->
                apiInterface.getAdsByUser(
                    userId = request.userId,
                    perPage = request.perPage,
                    page = request.page,
                ).data
            }
        )
        allAdsData = response
    }

    fun getAllSubMetals() = viewModelScope.launch {
        if (subMetalData == null) {
            val result = homeRepository.getAllSubMetals()
            if (result is Result.Success) {
                subMetalData = result.data
                suggestSubMetals = result.data.data
            } else if (result is Result.Failure) {
                error = result.exception.message ?: "Failure"
            }
        }
    }

    fun createPost(
        userId: String,
        metalName: String,
        phoneNumber: String,
        submetal: String,
        city: String,
        name: String,
        description: String,
        price: String,
        photos: List<MultipartBody.Part>
    ) = viewModelScope.launch {
        isLoading = true
        Log.d("lsdjg", "createPost: $photos")
        val result = homeRepository.createPost(
            userId = userId.toRequestBody(),
            metalName = metalName.toRequestBody(),
            phoneNumber = phoneNumber.toRequestBody(),
            submetal = submetal.toRequestBody(),
            city = city.toRequestBody(),
            name = name.toRequestBody(),
            description = description.toRequestBody(),
            price = price.toRequestBody(),
            photos = photos
        )
        isLoading = false
        Log.d("lsdjg", "Response: $result")
        if (result is Result.Success) {
            adPostResponse = result.data
        } else if (result is Result.Failure) {
            error = result.exception.message ?: "Failure"
        }
    }

    fun searchSubMetals(searchText: String) {
        suggestSubMetals = subMetalData?.data?.filter {
            it.name.contains(searchText, ignoreCase = true) ||
                    it.urduName.contains(searchText)
        } ?: listOf()
    }

    // Helper function to convert String to RequestBody
    private fun String.toRequestBody(): RequestBody {
        return this.toRequestBody("text/plain".toMediaTypeOrNull())
    }
}