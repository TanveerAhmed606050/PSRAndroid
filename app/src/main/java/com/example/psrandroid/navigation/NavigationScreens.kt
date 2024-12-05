package com.example.psrandroid.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

sealed class Screen(val route: String) {
    data object SplashScreen : Screen("splashScreen")
    data object LanguagesScreen : Screen("LanguageScreen")
    data object CityScreen : Screen("CityScreen")
    data object LoginScreen : Screen("loginScreen")
    data object OTPScreen : Screen("otpScreen")
    data object RegisterScreen : Screen("RegisterScreen")
    data object PasswordScreen : Screen("passwordScreen")
    data object AddProfileScreen : Screen("addProfileScreen")
    data object RateScreen : Screen("RateScreen")
    data object UpdatePasswordScreen : Screen("updatePassword")
    data object MyProfileScreen : Screen("profileScreen")
    data object PrimeUserScreen : Screen("primeUserScreen")
    data object LmeScreen : Screen("LmeScreen")
    data object HomeScreen : Screen("HomeScreen")
    data object AdScreen : Screen("AdScreen")
}

class PSRNavigationActions(private val navController: NavHostController) {

    fun navigateTo(destination: BottomNavigationItem) {
        navController.navigate(destination.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}