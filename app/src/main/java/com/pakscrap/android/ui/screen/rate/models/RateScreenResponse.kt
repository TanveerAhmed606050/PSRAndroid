package com.pakscrap.android.ui.screen.rate.models

import com.google.gson.annotations.SerializedName

data class RateScreenResponse(
    val status: Boolean,
    val data: List<RateData>,
    val message: String
)

data class RateData(
    val id: String,
    @SerializedName("metal_type")
    val metalType: List<SubMetal>,
    val name: String
)

data class SubMetal(
    val id: String,
    val name: String,
    val price: String
)