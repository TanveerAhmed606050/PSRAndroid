package com.pak.scrap.ui.screen.rate.models

import com.google.gson.annotations.SerializedName

data class MetalRateResponse(
    val data: List<MetalData>,
    val message: String,
    val status: Boolean
)

data class MetalData(
    @SerializedName("metal_name")
    val metalName: String,
    @SerializedName("metal_urdu_name")
    val metalUrduName: String,
    val submetals: List<SubMetals>
)

data class SubMetals(
    val id: String,
    @SerializedName("price_max")
    val priceMax: String,
    @SerializedName("price_min")
    val priceMin: String,
    @SerializedName("submetal_name")
    val submetalName: String,
    @SerializedName("submetal_urdu_name")
    val submetalUrduName: String
)



