package com.example.psrandroid.ui.commonViews

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.psrandroid.ui.theme.LightBlue
import com.example.psrandroid.ui.theme.regularFont
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AppButton(text: String, buttonColor: Color, textColor: Color, onclick: () -> Unit) {
    var isEnabled by remember { mutableStateOf(true) }
    Button(
        onClick = {
            if (isEnabled) {
                isEnabled = false
                onclick()
                CoroutineScope(Dispatchers.Main).launch {
                    delay(600) // disable for 1 second
                    isEnabled = true
                }
            }
        },
//        enabled = !isEnabled, // Disable the button if clicked
        colors = ButtonDefaults.buttonColors(buttonColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .height(45.dp)
            .border(
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(width = 1.dp, color = Color.LightGray),
            )
            .clip(RoundedCornerShape(12.dp))
            .background(
                LightBlue, shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp)
    )
    {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .padding(2.dp),
                fontSize = 17.sp,
                color = textColor,
                fontFamily = regularFont,
            )
        }
    }
}
