package com.pakscrap.response

import com.google.gson.annotations.SerializedName

data class SearchSubMetal(
    val data: List<SubMetalData>,
    val message: String,
    val status: Boolean
) {
    companion object
}

val SearchSubMetal.Companion.mockup by lazy {
    SearchSubMetal(data = listOf(), message = "quaeque", status = false)
}

data class SubMetalData(
    val id: Int,
    @SerializedName("submetal_name")
    val submetalName: String,
    val submetals: String,
    @SerializedName("submetal_urdu_name")
    val submetalUrduName: String,
    @SerializedName("price_min")
    val priceMin: String,
    @SerializedName("price_max")
    val priceMax: String,
) {
    companion object
}

val SubMetalData.Companion.mockup by lazy {
    SubMetalData(
        id = 1869,
        submetalName = "Dexter Booth",
        submetals = "no",
        submetalUrduName = "",
        priceMin = "85 ",
        priceMax = "85 "
    )
}