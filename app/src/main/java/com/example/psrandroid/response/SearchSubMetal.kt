package com.example.psrandroid.response

import com.google.gson.annotations.SerializedName

data class SearchSubMetal(
    val `data`: List<SubMetalData>,
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
    val name: String,
    val submetals: List<Any>,
    @SerializedName("urdu_name")
    val urduName: String
){companion object}
val SubMetalData.Companion.mockup by lazy {
    SearchSubMetal(data = listOf(), message = "suscipit", status = false)
}