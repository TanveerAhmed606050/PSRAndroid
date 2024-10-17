package com.example.psrandroid.ui.screen.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.psp_android.R
import com.example.psrandroid.navigation.Screen
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.ui.screen.auth.AuthVM
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
    val locationData = authViewModel.locationData
//    val dealersList = authViewModel.dealersList
    //call location & dealers List
    authViewModel.getLocation()
//    authViewModel.getDealersList()
    if (locationData?.status != false){
        if (locationData != null) {
            authViewModel.userPreferences.saveLocationList(locationData)
        }
        authViewModel.locationData = null
    }

    SplashScreen()
    LaunchedEffect(key1 = true) {
        delay(3000)
        if (authViewModel.userPreferences.isFirstLaunch) {
            navController.popBackStack(Screen.SplashScreen.route, true)
            navController.navigate(Screen.LoginScreen.route)
        } else {
            navController.popBackStack(Screen.SplashScreen.route, true)
            navController.navigate(Screen.DashBoardScreen.route)
        }
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_ic),
            contentDescription = "",
            contentScale = ContentScale.Crop, // Adjust this as needed
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    PSP_AndroidTheme {
        SplashScreen()
    }
}