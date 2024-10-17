package com.example.psrandroid.response

data class DealerResponse(
    val data: List<DealersData>,
    val message: String,
    val status: Boolean
)
data class DealersData(
    val email: String,
    val id: Int,
    val name: String
)