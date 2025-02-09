package com.example.psrandroid.ui.screen.adPost.models

data class ResponseCreatePost(
    val `data`: CreatePostData,
    val message: String,
    val status: Boolean
)

data class CreatePostData(
    val city: String,
    val created_at: String,
    val description: String,
    val id: Int,
    val metal_name: String,
    val name: String,
    val phone_number: String,
    val photos: List<String>,
    val price: Int,
    val submetal: String,
    val updated_at: String,
    val user_id: String,
    val video_path: Any
)