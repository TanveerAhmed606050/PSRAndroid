package com.example.psrandroid.ui.screen.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.psp_android.R
import com.example.psrandroid.navigation.Screen
import com.example.psrandroid.ui.screen.auth.AuthVM
import com.example.psrandroid.ui.screen.profile.switchLanguage
import com.example.psrandroid.ui.theme.AppBG
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.ui.theme.boldFont
import com.example.psrandroid.ui.theme.mediumFont
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(
    navController: NavController,
    authViewModel: AuthVM
) {
    val context = LocalContext.current
    authViewModel.getLocation()

    if (authViewModel.userPreferences.isUrduSelected)
        switchLanguage(context = context, "ur")
    else
        switchLanguage(context = context, "en")
    SplashScreen()
    LaunchedEffect(key1 = true) {
        delay(3000)
        if (!authViewModel.userPreferences.isTermsAccept) {
            navController.popBackStack(Screen.SplashScreen.route, true)
            navController.navigate(Screen.PrivacyPolicyScreen.route)
        } else if (authViewModel.userPreferences.isFirstLaunch) {
            navController.popBackStack(Screen.SplashScreen.route, true)
            navController.navigate(Screen.LoginScreen.route)
        } else {
            navController.popBackStack(Screen.SplashScreen.route, true)
            navController.navigate(Screen.HomeScreen.route)
        }
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBG),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = R.drawable.splash_ic), contentDescription = "",
                modifier = Modifier.fillMaxWidth())
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.from),
                fontSize = 18.sp,
                color = DarkBlue,
                fontFamily = mediumFont,
                modifier = Modifier.padding(bottom = 0.dp),
            )
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 18.sp,
                color = DarkBlue,
                fontFamily = boldFont,
                modifier = Modifier.padding(bottom = 20.dp),
            )
            Spacer(modifier = Modifier.padding(bottom = 30.dp))
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    PSP_AndroidTheme {
        SplashScreen()
    }
}