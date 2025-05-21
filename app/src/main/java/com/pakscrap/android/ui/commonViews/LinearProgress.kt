package com.pakscrap.android.ui.commonViews

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
import com.pakscrap.android.ui.theme.LightBlue
import kotlinx.coroutines.delay

@Composable
fun LinearProgress(modifier: Modifier = Modifier){
    var showLinearProgress by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(6000)
        showLinearProgress = false
    }

    if (showLinearProgress) {
        LinearProgressIndicator(
            modifier = modifier.fillMaxWidth(),
            color = LightBlue,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = StrokeCap.Round
        )
    }
}