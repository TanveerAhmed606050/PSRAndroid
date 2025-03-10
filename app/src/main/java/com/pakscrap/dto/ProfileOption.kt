package com.pakscrap.dto

import androidx.compose.ui.graphics.Color

data class ProfileOption(
    val iconId: Int,
    val title: String,
    val color: Color = Color.Black,
    val switch: Boolean = false
)
