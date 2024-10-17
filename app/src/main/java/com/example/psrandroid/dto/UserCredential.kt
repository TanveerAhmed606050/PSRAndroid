package com.example.psrandroid.dto

import com.google.gson.annotations.SerializedName

data class UserCredential(
    val phone:String,
    val password:String = "",
    val name:String = ""
)
