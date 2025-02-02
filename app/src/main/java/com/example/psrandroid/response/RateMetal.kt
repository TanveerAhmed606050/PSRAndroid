package com.example.psrandroid.response

import com.google.gson.annotations.SerializedName

data class RateMetal(
    val data: List<MetalData>,
    val message: String,
    val status: Boolean
){companion object}
val RateMetal.Companion.mockup by lazy { RateMetal(
    data = listOf(),
    message = "hinc",
    status = false
) }
data class MetalData(
    val description: String,
    val id: Int,
    val name: String,
    @SerializedName("urdu_name")
    val urduName: String
)
{companion object}
val MetalData.Companion.mockup by lazy {
    MetalData(
        description = "vestibulum",
        id = 7552,
        name = "Stan Bolton",
        urduName = "Cheri Merrill"
    )
}