package com.example.psrandroid.response

import com.google.gson.annotations.SerializedName

data class DashboardResponse(
    val status: Boolean,
    val data: List<DashboardData>,
    val message: String
)

data class DashboardData(
    val id: String,
    @SerializedName("metal_type")
    val metalType: List<MetalType>,
    val name: String
)

data class MetalType(
    val id: String,
    val name: String,
    val price: String
)