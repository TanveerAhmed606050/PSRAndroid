package com.pakscrap.ui.screen.rate.models

import com.google.gson.annotations.SerializedName

data class RateSuggestionResponse(
    val data: List<MainMetalData>,
    val message: String,
    val status: Boolean
)

data class MainMetalData(
    @SerializedName("created_at")
    val createdAt: String,
    val description: String,
    val id: Int,
    val name: String,
    val slug: String,
    val submetal: List<RateSubmetal>,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("urdu_name")
    val urduName: String
)

data class RateSubmetal(
    @SerializedName("created_at")
    val createdAt: String,
    val description: String,
    val id: Int,
    @SerializedName("metal_id")
    val metalId: String,
    val name: String,
    val slug: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("urdu_name")
    val urduName: String
)