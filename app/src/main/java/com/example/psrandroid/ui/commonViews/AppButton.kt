package com.example.psrandroid.ui.commonViews

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.mediumFont

@Composable
fun AppButton(
    modifier: Modifier, text: String, isEnable: Boolean = true,
    onButtonClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        onClick = { onButtonClick() },
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPressed) DarkBlue.copy(alpha = 0.7f) else DarkBlue
        ),
        shape = RoundedCornerShape(10.dp),
        interactionSource = interactionSource,
        enabled = isEnable,
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = Color.White,
            fontFamily = mediumFont,
            textAlign = TextAlign.Center,
        )
    }
}
