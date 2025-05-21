package com.pakscrap.android.ui.screen.profile.models

import androidx.compose.ui.graphics.Color

data class ProfileOption(
    val iconId: Int,
    val title: String,
    val color: Color = Color.Black,
    val switch: Boolean = false
)
