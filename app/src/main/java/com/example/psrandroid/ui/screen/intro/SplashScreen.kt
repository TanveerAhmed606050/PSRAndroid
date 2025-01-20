package com.example.psrandroid.ui.screen.intro

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.psp_android.R
import com.example.psrandroid.navigation.Screen
import com.example.psrandroid.ui.screen.auth.AuthVM
import com.example.psrandroid.ui.theme.AppBG
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.LightBlue
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.ui.theme.boldFont
import com.example.psrandroid.ui.theme.mediumFont
import kotlinx.coroutines.delay

val isInPreview @Composable get() = LocalInspectionMode.current

@Composable
fun Modifier.drawPreviewBorder(color: Color = Color.Gray): Modifier {
    return if (isInPreview) {
        this then Modifier.border(width = 1.dp, color = color)
    } else {
        this
    }
}

@Composable
fun SplashScreen(
    navController: NavController,
    authViewModel: AuthVM
) {
    //login api response
//    val locationData = authViewModel.locationData
//    val dealersList = authViewModel.dealersList
    //call location & dealers List
    authViewModel.getLocation()
//    authViewModel.getDealersList()
//    if (locationData?.status != false){
//        if (locationData != null) {
//            authViewModel.userPreferences.saveLocationList(locationData)
//        }
//        authViewModel.locationData = null
//    }

    SplashScreen()
    LaunchedEffect(key1 = true) {
        delay(3000)
        if (!authViewModel.userPreferences.isTermsAccept){
            navController.popBackStack(Screen.SplashScreen.route, true)
            navController.navigate(Screen.PrivacyPolicyScreen.route)
        }
        else if (authViewModel.userPreferences.isFirstLaunch) {
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
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 20.sp,
                color = DarkBlue,
                fontFamily = boldFont,
                modifier = Modifier.padding(bottom = 0.dp),
            )
            Text(
                text = stringResource(id = R.string.scrap_rate),
                fontSize = 18.sp,
                color = DarkBlue,
                fontFamily = mediumFont,
                modifier = Modifier.padding(bottom = 40.dp),
            )
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
            Spacer(modifier = Modifier.padding(bottom = 20.dp))
        }
    }
//        Image(
//            painter = painterResource(id = R.drawable.splash_ic),
//            contentDescription = "",
//            contentScale = ContentScale.Crop, // Adjust this as needed
//            modifier = Modifier.fillMaxSize()
//        )
//}
}

@Preview
@Composable
fun SplashScreenPreview() {
    PSP_AndroidTheme {
        SplashScreen()
    }
}