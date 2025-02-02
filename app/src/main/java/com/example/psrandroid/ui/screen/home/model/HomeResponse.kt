package com.example.psrandroid.ui.screen.home.model

import com.example.psrandroid.response.LmeData
import com.example.psrandroid.response.SubMetal
import com.example.psrandroid.ui.screen.adPost.models.AdData
import com.google.gson.annotations.SerializedName

data class HomeResponse(
    val data: HomeData,
    val message: String,
    val status: Boolean
)

data class HomeData(
    val Ads: List<AdData>,
    val LMEMetals: List<LmeData>,
    val subMetals: List<SubMetal>
)

