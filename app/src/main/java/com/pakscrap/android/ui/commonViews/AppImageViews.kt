package com.pakscrap.android.ui.commonViews

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pakscrap.R
import com.pakscrap.android.utils.Constant

@Composable
fun MyAsyncImage(imageUrl: String?, size: Dp, isCircular: Boolean) {
    val imageShape = if (isCircular) CircleShape else RoundedCornerShape(8.dp)
    val placeHolder = painterResource(id = R.drawable.user_placeholder)

    AsyncImage(
        model = Constant.MEDIA_BASE_URL + imageUrl,
        contentDescription = "",
        placeholder = placeHolder,
        error = placeHolder,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(size)
            .clip(imageShape)
            .border(1.dp, Color.LightGray, imageShape)
    )
}
