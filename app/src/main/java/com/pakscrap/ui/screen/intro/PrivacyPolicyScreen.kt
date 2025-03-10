package com.pakscrap.ui.screen.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pakscrap.navigation.Screen
import com.pakscrap.R
import com.pakscrap.ui.commonViews.AppButton
import com.pakscrap.ui.commonViews.WhiteButton
import com.pakscrap.ui.screen.auth.AuthVM
import com.pakscrap.ui.theme.AppBG
import com.pakscrap.ui.theme.DarkBlue
import com.pakscrap.ui.theme.PSP_AndroidTheme
import com.pakscrap.ui.theme.boldFont
import com.pakscrap.ui.theme.mediumFont
import com.pakscrap.ui.theme.regularFont

@Composable
fun PrivacyPolicyScreen(navController: NavController, authVM: AuthVM) {
    PrivacyPolicyViews(onAgreeClick = {
        authVM.userPreferences.isTermsAccept = true
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
            .background(DarkBlue),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = R.drawable.pak_scrap_copy), contentDescription = "",
                modifier = Modifier.fillMaxWidth()
                    .height(400.dp)
                    .padding(horizontal = 20.dp))
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
                fontFamily = regularFont,
                modifier = Modifier.padding(bottom = 0.dp),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                letterSpacing = 2.sp,
            )
            WhiteButton(
                modifier = Modifier.padding(vertical = 20.dp),
                text = stringResource(id = R.string.agree_continue),
                onButtonClick = { onAgreeClick() }
            )
            Spacer(modifier = Modifier.padding(bottom = 40.dp))
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
