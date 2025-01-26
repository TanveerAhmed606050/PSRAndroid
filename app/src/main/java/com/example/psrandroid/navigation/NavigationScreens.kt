package com.example.psrandroid.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

sealed class Screen(val route: String) {
    data object SplashScreen : Screen("splashScreen")
    data object PrivacyPolicyScreen : Screen("privacyPoliceScreen")
    data object LanguagesScreen : Screen("LanguageScreen")
    data object LoginScreen : Screen("loginScreen")
    data object RegisterScreen : Screen("RegisterScreen")
    data object PasswordScreen : Screen("passwordScreen")
    data object AddProfileScreen : Screen("addProfileScreen")
    data object RateScreen : Screen("RateScreen")
    data object UpdatePasswordScreen : Screen("updatePassword")
    data object MyProfileScreen : Screen("profileScreen")
    data object PrimeUserScreen : Screen("primeUserScreen")
    data object LmeScreen : Screen("LmeScreen")
    data object HomeScreen : Screen("HomeScreen")
    data object MyPostScreen : Screen("MyPostScreen")
    data object AllPostScreen : Screen("AllPostScreen")
    data object AdScreen : Screen("AdScreen")
    data object AdDetailScreen : Screen("AdDetailScreen")
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