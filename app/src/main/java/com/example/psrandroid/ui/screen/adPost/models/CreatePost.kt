package com.example.psrandroid.ui.screen.adPost.models

import com.google.gson.annotations.SerializedName

data class CreatePost(
    @SerializedName("metal_name")
    val metalName: String,
    val submetal: String,
    val city: String,
    val name: String,
    val description: String,
    val price: String,
    val photos: List<String>,
)
