package com.example.psrandroid.dto

import com.google.gson.annotations.SerializedName

data class ImageUpdate(
    @SerializedName("user_id")
    val userId:Int,
    val image:String,
)
