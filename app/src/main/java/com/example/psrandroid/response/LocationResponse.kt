package com.example.psrandroid.response

import com.google.gson.annotations.SerializedName

data class LocationResponse(
    val data: List<LocationData>,
    val message: String,
    val status: Boolean
)
data class LocationData(
    val id: Int,
    val name: String,
    @SerializedName("urdu_name")
    val urduName: String
){
    companion object
}
val LocationData.Companion.mockup by lazy {
LocationData(id = 6777, name = "Freida Johns", urduName = "Perry Erickson")
}