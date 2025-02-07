package com.example.psrandroid

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.psp_android.R
import com.example.psrandroid.navigation.PSRNavigation
import com.example.psrandroid.navigation.Screen
import com.example.psrandroid.storage.UserPreferences
import com.example.psrandroid.ui.commonViews.LogoutDialog
import com.example.psrandroid.ui.commonViews.loadRewardedAd
import com.example.psrandroid.ui.screen.rate.RateVM
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.utils.LogoutSession
import com.example.psrandroid.utils.Utils.actionBar
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var userPreference: UserPreferences

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar(this)
        enableEdgeToEdge()
        setContent {
            //Load Rewarded Ads Launch Effect
            val rateVm = hiltViewModel<RateVM>()
            loadRewardedAd(
                context = this,
                getString(R.string.rewarded_ad_unit_id),
                onAdLoaded = {
                    rateVm.rewardedAd = it
                })
            if (rateVm.watchAd) {
                LaunchedEffect(key1 = rateVm.watchAd) {
                    delay(15 * 60 * 1000) // 15 minutes delay
                    rateVm.watchAd = false // Reset watchAd to false
                }
            }

            val scope = rememberCoroutineScope()
            val navController = rememberNavController()
            val windowSize = calculateWindowSizeClass(this)
            val displayFeatures = calculateDisplayFeatures(this)
            var isLogout by remember { mutableStateOf(false) }
            PSP_AndroidTheme {
                SetupSystemUi()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PSRNavigation(
                        navController = navController,
                        windowSize = windowSize,
                        displayFeatures = displayFeatures,
                        onLogOut = {
                            isLogout = true
//                            profileViewModel.clearUserCredential()
//                            finish()
                        }
                    )
//                    PSRNavHost(navController = navController)
                }
            }
            if (isLogout) LogoutDialog(onDismissRequest = {
                isLogout = false
            }, onOkClick = {
                isLogout = false
                userPreference.clearStorage()
                LogoutSession.clearError()
                navController.navigate(Screen.LoginScreen.route) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            })

            LaunchedEffect(LogoutSession.errorMessages) {
                scope.launch {
                    LogoutSession.errorMessages.collect { errorMessage ->
                        errorMessage?.let {
                            if (it.contains("Unauthorized") || it.contains("401")) {
                                isLogout = true
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SetupSystemUi() {
    val systemUiController = rememberSystemUiController()

    // Set navigation bar color and icon theme
    systemUiController.setNavigationBarColor(
        color = Color.Transparent, // Replace with your desired color
        transformColorForLightContent = { Color.Black },
        navigationBarContrastEnforced = true,
        darkIcons = true // Use true for dark icons, false for light icons
    )

    // Optionally, set the status bar color as well
    systemUiController.setStatusBarColor(
        color = Color.Transparent,
        transformColorForLightContent = { Color.Black },
        darkIcons = true, // Use true for dark icons, false for light icons
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PSP_AndroidTheme {
    }
}