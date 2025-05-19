package com.pakscrap.ui.screen.adPost.models

import com.google.gson.annotations.SerializedName

data class MyAds(
    val data: List<AdsData>,
    val message: String,
    val status: Boolean
) {
    companion object
}

val MyAds.Companion.mockup by lazy {
    MyAds(data = listOf(), message = "success", status = false)
}

data class AdsData(
    @SerializedName("approval_status")
    val approvalStatus: String,
    val city: String,
    @SerializedName("created_at")
    val createdAt: String,
    val description: String,
    val id: Int,
    @SerializedName("metal_name")
    val metalName: String,
    val name: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    val photos: List<String>,
    val price: String,
    val status: String,
    val submetal: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("video_path")
    val videoPath: String
) {
    companion object
}

val AdsData.Companion.mockup by lazy {
    AdsData(
        approvalStatus = "false",
        city = "Lahore",
        createdAt = "1 pm",
        description = "demo description",
        id = 3279,
        metalName = "Iron",
        name = "Kanchi tok",
        phoneNumber = "+923451234567",
        photos = listOf(),
        price = "12",
        status = "false",
        submetal = "scrap",
        updatedAt = "12/12/25",
        userId = "123",
        videoPath = ""
    )
}