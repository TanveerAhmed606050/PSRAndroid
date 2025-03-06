package com.pak.scrap.ui.screen.adPost.models

import com.google.gson.annotations.SerializedName

data class ResponseCreatePost(
    val `data`: CreatePostData,
    val message: String,
    val status: Boolean
)

data class CreatePostData(
    val city: String,
    @SerializedName("created_at")
    val createdAt: String,
    val description: String,
    val id: Int,
    @SerializedName("metal_name")
    val metalName: String,
    val name: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    val photos: List<String>,
    val price: Int,
    val submetal: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("video_path")
    val videoPath: String
)