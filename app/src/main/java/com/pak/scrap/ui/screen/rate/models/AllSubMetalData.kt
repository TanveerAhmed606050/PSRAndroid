package com.pak.scrap.ui.screen.rate.models

import com.google.gson.annotations.SerializedName

data class AllSubMetalData(
    val data: List<SubData>,
    val message: String,
    val status: Boolean
)
data class SubData(
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