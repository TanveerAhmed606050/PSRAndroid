package com.example.psrandroid.ui.screen.adPost.models

import com.google.gson.annotations.SerializedName

data class AllAds(
    val data: List<AdData>,
    val message: String,
    val status: Boolean
) {
    companion object
}

val AllAds.Companion.mockup by lazy {
    AllAds(data = listOf(), message = "repudiandae", status = false)
}

data class AdData(
    val city: String,
    @SerializedName("created_at")
    val createdAt: String,
    val description: String,
    val id: Int,
    @SerializedName("metal_name")
    val metalName: String,
    val name: String,
    val photos: String,
    val price: String,
    val submetal: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("video_path")
    val videoPath: String = ""
) {
    companion object
}

val AdData.Companion.mockup by lazy {
    AdData(
        city = "Golden Coast",
        createdAt = "inani",
        description = "Description",
        id = 4574,
        metalName = "Iron",
        name = "Scrap Iron",
        photos = "",
        price = "6 Lac",
        submetal = "Kanchi Tok",
        updatedAt = "vidisse",
        videoPath = "similique"
    )
}