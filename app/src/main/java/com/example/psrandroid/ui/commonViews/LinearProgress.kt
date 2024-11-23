package com.example.psrandroid.ui.commonViews

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.colorResource
import com.example.psp_android.R
import com.example.psrandroid.ui.theme.LightBlue
import kotlinx.coroutines.delay

@Composable
fun LinearProgress(modifier: Modifier = Modifier){
    var showProgress by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(6000) // Delay for 6 seconds
        showProgress = false
    }

    if (showProgress) {
        LinearProgressIndicator(
            modifier = modifier.fillMaxWidth(),
            color = LightBlue,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = StrokeCap.Round
        )
    }
}