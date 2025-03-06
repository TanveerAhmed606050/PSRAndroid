package com.pak.scrap.response

import com.google.gson.annotations.SerializedName

data class PrimeUser(
    val data: List<PrimeUserData>,
    val message: String,
    val status: Boolean
) {
    companion object
}

val PrimeUser.Companion.mockup by lazy {
    PrimeUser(data = listOf(), message = "", status = false)
}

data class PrimeUserData(
    @SerializedName("business_name")
    val businessName: String,
    @SerializedName("created_at")
    val createdAt: String,
    val email: String,
    @SerializedName("email_verified_at")
    val emailVerifiedAt: String?,
    val id: Int,
    val location: String,
    val name: String,
    @SerializedName("profile_image")
    val profileImage: String?,
    val type: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val whatsapp: String
) {
    companion object
}

val PrimeUserData.Companion.mockup by lazy {
    PrimeUserData(
        businessName = "Susana Hinton",
        createdAt = "facilisis",
        email = "nadine.walton@example.com",
        emailVerifiedAt = null,
        id = 4947,
        location = "solum",
        name = "Elaine Benton",
        profileImage = null,
        type = "curabitur",
        updatedAt = "neglegentur",
        whatsapp = "periculis"
    )
}