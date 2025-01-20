package com.example.psrandroid.ui.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.psp_android.R
import com.example.psrandroid.navigation.Screen
import com.example.psrandroid.ui.commonViews.Header
import com.example.psrandroid.ui.theme.AppBG
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.PSP_AndroidTheme

@Composable
fun PasswordScreen(navController: NavController) {
    PasswordScreen(backClick = {
        navController.popBackStack()
    },
        onNextClick = {
            navController.navigate(Screen.RateScreen.route)
        })
}

@Composable
fun PasswordScreen(backClick: () -> Unit, onNextClick: () -> Unit) {
    var pinValue by rememberSaveable { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBG)
    ) {
//        Image(
//            painter = painterResource(id = R.drawable.lang_city_bg),
//            contentDescription = null,
//            contentScale = ContentScale.Crop, // Adjust this as needed
//            modifier = Modifier.fillMaxSize(),
//        )
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(Modifier.statusBarsPadding())
            Header(
                modifier = null,
                stringResource(id = R.string.enter_pin),
                backClick = { backClick() })
            Spacer(modifier = Modifier.height(100.dp))
            PinTextField(otpText = pinValue, otpCount = 6, onOtpTextChange = { value, _ ->
                pinValue = value
            })
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .clickable { onNextClick() }
        ) {
            Row(
                modifier = Modifier
                    .background(color = DarkBlue, shape = CircleShape)
                    .size(50.dp)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_navigate_next_24),
                    contentDescription = null
                )
            }
        }    }
}

@Composable
fun PinTextField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpCount: Int ,
    onOtpTextChange: (String, Boolean) -> Unit
) {
    LaunchedEffect(Unit) {
        if (otpText.length > otpCount) {
            throw IllegalArgumentException("Otp text value must not have more than otpCount: $otpCount characters")
        }
    }

    BasicTextField(
        modifier = modifier,
        value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
        onValueChange = {
            if (it.text.length <= otpCount) {
                onOtpTextChange.invoke(it.text, it.text.length == otpCount)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(otpCount) { index ->
                    CharView(
                        index = index,
                        text = otpText,
                        modifier = Modifier
                            .weight(1f) // Ensure equal width for each CharView
                            .aspectRatio(0.8f) // Optional: Adjust aspect ratio for better appearance
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    )
}

@Composable
private fun CharView(
    index: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    val char = when {
        index == text.length -> ""
        index > text.length -> ""
        else -> text[index].toString()
    }

    Text(
        modifier = modifier
//            .width(40.dp)
//            .height(50.dp)
            .drawBehind {
                val strokeWidth = 2.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = DarkBlue,
                    start = androidx.compose.ui.geometry.Offset(0f, y),
                    end = androidx.compose.ui.geometry.Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            }
            .padding(top = 12.dp),
        text = char,
        style = MaterialTheme.typography.titleLarge,
        color = DarkBlue,
        textAlign = TextAlign.Center,
        fontSize = 16.sp
    )
}

@Preview
@Composable
fun PreviewPasswordScreen() {
    PSP_AndroidTheme {
        PasswordScreen(backClick = {}, onNextClick = {})
    }
}