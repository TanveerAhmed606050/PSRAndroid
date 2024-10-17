package com.example.psrandroid.ui.commonViews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.psp_android.R
import com.example.psrandroid.ui.theme.LightBlue
import com.example.psrandroid.ui.theme.mediumFont

@Composable
fun AppButton(modifier: Modifier, text: String, onButtonClick: () -> Unit) {
//    var isEnabled by remember { mutableStateOf(true) }
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        onClick = { onButtonClick() },
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.white)
        ),
        shape = RoundedCornerShape(246.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 17.62.sp,
                fontWeight = FontWeight.Bold,
                color = LightBlue,
                fontFamily = mediumFont,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}
