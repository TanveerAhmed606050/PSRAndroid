package com.example.psrandroid.response

import com.google.gson.annotations.SerializedName

data class LmeResponse(
    val data: List<LmeData>,
    val message: String,
    val status: Boolean
)

data class LmeData(
    @SerializedName("change_in_rate")
    val changeInRate: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("expiry_date")
    val expiryDate: String,
    val id: Int,
    val name: String,
    val price: String,
    @SerializedName("updated_at")
    val updatedAt: String
) {
    companion object
}

val LmeData.Companion.mockup by lazy {
    LmeData(
        changeInRate = "-1.5",
        createdAt = "no",
        expiryDate = "2025-02-06T00:00:00.000000Z",
        id = 6163,
        name = "Copper",
        price = "12",
        updatedAt = "quas"
    )
}
