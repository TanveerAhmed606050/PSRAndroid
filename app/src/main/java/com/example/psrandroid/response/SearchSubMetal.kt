package com.example.psrandroid.response

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
    val price_min: String,
    val price_max: String,
) {
    companion object
}

val SubMetalData.Companion.mockup by lazy {
    SubMetalData(
        id = 1869,
        submetalName = "Dexter Booth",
        submetals = "no",
        submetalUrduName = "",
        price_min = "85 ",
        price_max = "85 "
    )
//    SearchSubMetal(data = listOf(), message = "suscipit", status = false)
}