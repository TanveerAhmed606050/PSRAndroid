package com.pakscrap.android

import android.content.Context
import android.os.Build
import android.os.Bundle
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import com.pakscrap.android.navigation.PSRNavigation
import com.pakscrap.android.navigation.Screen
import com.pakscrap.android.storage.UserPreferences
import com.pakscrap.android.ui.commonViews.GoogleInterstitialAd
import com.pakscrap.android.ui.commonViews.LogoutDialog
import com.pakscrap.android.ui.commonViews.loadRewardedAd
import com.pakscrap.android.ui.screen.rate.RateVM
import com.pakscrap.android.ui.theme.PSP_AndroidTheme
import com.pakscrap.android.utils.LocaleHelper
import com.pakscrap.android.utils.LogoutSession
import com.pakscrap.android.utils.Utils.actionBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.pakscrap.R

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var userPreference: UserPreferences
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar(this)
        enableEdgeToEdge()
        subscribeTopic()
        firebaseAnalytics = Firebase.analytics

        setContent {
            val rateVm = hiltViewModel<RateVM>()
            GoogleInterstitialAd(this, onAdClick = {})
            loadRewardedAd(
                context = this,
                getString(R.string.rewarded_ad_unit_id),
                onAdLoaded = {
                    rateVm.rewardedAd = it
                })

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
                        }
                    )
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

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(
            LocaleHelper.updateLocale(
                newBase,
                LocaleHelper.getLanguage(newBase)
            )
        )
    }
}

@Composable
fun SetupSystemUi() {
    val systemUiController = rememberSystemUiController()

    systemUiController.setNavigationBarColor(
        color = Color.Transparent,
        transformColorForLightContent = { Color.Black },
        navigationBarContrastEnforced = true,
        darkIcons = true
    )

    systemUiController.setStatusBarColor(
        color = Color.Transparent,
        transformColorForLightContent = { Color.Black },
        darkIcons = true,
    )
}

fun subscribeTopic() {
    FirebaseMessaging.getInstance().subscribeToTopic("news")
        .addOnCompleteListener {
        }
}