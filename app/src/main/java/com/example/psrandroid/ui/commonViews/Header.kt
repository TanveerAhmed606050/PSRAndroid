package com.example.psrandroid.ui.commonViews

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.psp_android.R
import com.example.psrandroid.ui.theme.mediumFont

@Composable
fun Header(modifier: Modifier?,headerText:String, backClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .padding(6.dp)
                .clickable { backClick() }.align(Alignment.CenterStart)

        )
        Text(
            text = headerText, color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.Center),
            fontFamily = mediumFont
        )
    }
}