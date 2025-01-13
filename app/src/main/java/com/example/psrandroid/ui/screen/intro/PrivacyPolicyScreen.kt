package com.example.psrandroid.ui.screen.intro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.psp_android.R
import com.example.psrandroid.navigation.Screen
import com.example.psrandroid.ui.commonViews.AppButton
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.LightBlue
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.ui.theme.boldFont
import com.example.psrandroid.ui.theme.mediumFont

@Composable
fun PrivacyPolicyScreen(navController: NavController) {
    PrivacyPolicyViews(onAgreeClick = {
        navController.popBackStack()
        navController.navigate(Screen.LoginScreen.route)
    })
}

@Composable
fun PrivacyPolicyViews(
    onAgreeClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(LightBlue, DarkBlue))),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 20.sp,
                color = Color.White,
                fontFamily = boldFont,
                modifier = Modifier.padding(bottom = 0.dp),
            )
            Text(
                text = stringResource(id = R.string.scrap_rate),
                fontSize = 18.sp,
                color = Color.White,
                fontFamily = mediumFont,
                modifier = Modifier.padding(bottom = 40.dp),
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = R.string.privacy_terms),
                fontSize = 18.sp,
                color = Color.White,
                fontFamily = mediumFont,
                modifier = Modifier.padding(bottom = 0.dp),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                letterSpacing = 2.sp,
            )
            AppButton(
                modifier = Modifier.padding(vertical = 20.dp),
                text = stringResource(id = R.string.agree_continue),
                onButtonClick = { onAgreeClick() }
            )
        }
    }
}

@Composable
@Preview
fun PrivacyPolicyPreview() {
    PSP_AndroidTheme {
        PrivacyPolicyViews(onAgreeClick = {})
    }
}
