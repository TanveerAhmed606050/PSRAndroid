package com.pakscrap.ui.screen.auth.models

data class InfoDataResponse(
    val data: InfoData,
    val message: String,
    val status: Boolean
)

class InfoData