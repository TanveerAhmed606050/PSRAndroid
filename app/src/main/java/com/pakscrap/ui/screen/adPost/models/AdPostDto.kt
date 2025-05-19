package com.pakscrap.ui.screen.adPost.models

import com.google.gson.annotations.SerializedName

data class AdPostDto(
    @SerializedName("user_id")
    val userId: String = "",
    @SerializedName("metal_name")
    val metalName: String = "",
    @SerializedName("per_page")
    val perPage: String = "",
    val page: String = "",
    val city: String = "",
)
