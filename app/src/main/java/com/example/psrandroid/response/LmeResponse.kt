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
){companion object}
val LmeData.Companion.mockup by lazy{
    LmeData(
        changeInRate = "mnesarchum",
        createdAt = "no",
        expiryDate = "2025-12-11",
        id = 6163,
        name = "Betsy Knapp",
        price = "animal",
        updatedAt = "quas"
    )
}
