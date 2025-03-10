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
    MyAds(data = listOf(), message = "viris", status = false)
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
        approvalStatus = "reprimique",
        city = "Note",
        createdAt = "nihil",
        description = "augue",
        id = 3279,
        metalName = "Moises Patton",
        name = "Silvia Ratliff",
        phoneNumber = "(406) 146-9575",
        photos = listOf(),
        price = "voluptatum",
        status = "sapien",
        submetal = "utroque",
        updatedAt = "indoctum",
        userId = "semper",
        videoPath = ""
    )
}