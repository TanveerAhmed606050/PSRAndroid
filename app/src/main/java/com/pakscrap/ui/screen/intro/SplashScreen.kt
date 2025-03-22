package com.pakscrap.ui.screen.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pakscrap.R
import com.pakscrap.navigation.Screen
import com.pakscrap.ui.screen.auth.AuthVM
import com.pakscrap.ui.theme.DarkBG
import com.pakscrap.ui.theme.DarkBlue
import com.pakscrap.ui.theme.PSP_AndroidTheme
import com.pakscrap.ui.theme.boldFont
import com.pakscrap.ui.theme.regularFont
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(
    navController: NavController,
    authViewModel: AuthVM
) {
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
            .background(DarkBG),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_splash), contentDescription = "",
                modifier = Modifier
                    .height(200.dp)
                    .width(200.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Text(
                    text = stringResource(id = R.string.pak),
                    color = Color.White, fontFamily = boldFont,
                    fontSize = 36.sp
                )
                Spacer(modifier = Modifier.padding(start = 8.dp))
                Text(
                    text = stringResource(id = R.string.scrap),
                    color = DarkBlue, fontFamily = boldFont,
                    fontSize = 36.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.splash_1),
                fontSize = 18.sp,
                color = Color.White,
                fontFamily = boldFont,
            )
            Text(
                text = stringResource(id = R.string.splash_2),
                fontSize = 14.sp,
                color = Color.White,
                fontFamily = regularFont,
            )
        }
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Bottom,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = stringResource(id = R.string.from),
//                fontSize = 18.sp,
//                color = Color.White,
//                fontFamily = mediumFont,
//                modifier = Modifier.padding(bottom = 0.dp),
//            )
//            Text(
//                text = stringResource(id = R.string.pak_scrap_rate),
//                fontSize = 18.sp,
//                color = Color.White,
//                fontFamily = boldFont,
//                modifier = Modifier.padding(bottom = 20.dp),
//            )
//            Spacer(modifier = Modifier.padding(bottom = 30.dp))
//        }
    }
}


@Preview
@Composable
fun SplashScreenPreview() {
    PSP_AndroidTheme {
        SplashScreen()
    }
}