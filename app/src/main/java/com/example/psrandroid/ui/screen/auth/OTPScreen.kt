package com.example.psrandroid.ui.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.psp_android.R
import com.example.psrandroid.navigation.Screen
import com.example.psrandroid.ui.commonViews.Header
import com.example.psrandroid.ui.theme.PSP_AndroidTheme

@Composable
fun OTPScreen(navController: NavController, authVM: AuthVM) {
    OTPScreen(backClick = { navController.popBackStack() }, onNextClick = {
        navController.navigate(Screen.PasswordScreen.route)
    })
}

@Composable
fun OTPScreen(backClick: () -> Unit, onNextClick: () -> Unit) {
    var otpValue by rememberSaveable { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = R.drawable.lang_city_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop, // Adjust this as needed
            modifier = Modifier.fillMaxSize(),
        )
        Column(modifier = Modifier.padding(horizontal = 4.dp)) {
            Spacer(modifier = Modifier.height(40.dp))
            Header(
                modifier = null,
                stringResource(id = R.string.enter_verify),
                backClick = { backClick() })
            Spacer(modifier = Modifier.height(100.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                PinTextField(otpText = otpValue, otpCount = 6, onOtpTextChange = { value, _ ->
                    otpValue = value
                })
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .clickable { onNextClick() }
        ) {
            Row(
                modifier = Modifier
                    .background(color = Color.Blue, shape = CircleShape)
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
        }
    }
}

@Preview
@Composable
fun PreviewOTPScreen() {
    PSP_AndroidTheme {
        OTPScreen(backClick = {}, onNextClick = {})
    }
}