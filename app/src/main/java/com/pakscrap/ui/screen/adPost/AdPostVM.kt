package com.pakscrap.ui.screen.adPost

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pakscrap.dto.AdPostDto
import com.pakscrap.network.ApiInterface
import com.pakscrap.repository.GenericPagingRepository
import com.pakscrap.repository.HomeRepository
import com.pakscrap.storage.UserPreferences
import com.pakscrap.ui.screen.adPost.models.AdsData
import com.pakscrap.ui.screen.adPost.models.ResponseCreatePost
import com.pakscrap.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
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
    var adPostResponse by mutableStateOf<ResponseCreatePost?>(null)
    var allAdsData by mutableStateOf<Flow<PagingData<AdsData>>>(flowOf(PagingData.empty()))
    private var currentRequest: AdPostDto? = null

    private var _locationAds = MutableStateFlow<PagingData<AdsData>>(PagingData.empty())

    val locationAds: StateFlow<PagingData<AdsData>> get() = _locationAds

    fun getAdsByLocation(adPostDto: AdPostDto) {
        if (currentRequest?.city == adPostDto.city && currentRequest?.metalName == adPostDto.metalName) return // Prevent unnecessary reloads
        currentRequest = adPostDto
        viewModelScope.launch {
            genericPagingRepository.getPagingData(
                requestData = adPostDto,
                updateRequest = { request, page -> request.copy(page = page.toString()) },
                fetchData = { request ->
                    apiInterface.getAllAds(
                        city = request.city,
                        metalName = request.metalName,
                        perPage = request.perPage,
                        page = request.page
                    ).data
                }
            ).cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _locationAds.value = pagingData
                }
        }
    }

    fun getAdsByUserid(adPostDto: AdPostDto) = viewModelScope.launch {
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
        if (result is Result.Success) {
            adPostResponse = result.data
        } else if (result is Result.Failure) {
            error = result.exception.message ?: "Failure"
        }
    }

    // Helper function to convert String to RequestBody
    private fun String.toRequestBody(): RequestBody {
        return this.toRequestBody("text/plain".toMediaTypeOrNull())
    }
}