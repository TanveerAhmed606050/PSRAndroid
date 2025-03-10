package com.pakscrap.ui.screen.home.model

import com.google.gson.annotations.SerializedName
import com.pakscrap.ui.screen.adPost.models.AdsData

data class HomeResponse(
    val data: HomeData,
    val message: String,
    val status: Boolean
)

data class HomeScrapRate(
    @SerializedName("location_id")
    val locationId: Int,
    @SerializedName("location_name")
    val locationName: String,
    val rates: List<Rate>
)

data class HomeData(
    @SerializedName("home_scrap_rates")
    val homeScrapRates: List<HomeScrapRate>,
    @SerializedName("lme_metals")
    val lmeMetals: List<LmeMetal>,
    val posts: List<AdsData>
)

data class LmeMetal(
    val changeInRate: String,
    val createdAt: String,
    @SerializedName("expiry_date")
    val expiryDate: String,
    val id: Int,
    val name: String,
    val price: String,
    val updatedAt: String
){
    companion object
}

val LmeMetal.Companion.mockup by lazy {
    LmeMetal(
        changeInRate = "1%",
        createdAt = "",
        expiryDate = "21",
        id = 1795,
        name = "Iron",
        price = "123",
        updatedAt = ""
    )
}

data class Rate(
    val id: String,
    val metalName: String,
    val name: String,
    val price: String,
    val submetal: String
)