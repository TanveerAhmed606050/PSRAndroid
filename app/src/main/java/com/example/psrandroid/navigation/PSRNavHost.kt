package com.example.psrandroid.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.psrandroid.ui.screen.auth.AuthVM
import com.example.psrandroid.ui.screen.auth.CityScreen
import com.example.psrandroid.ui.screen.auth.LanguageScreen
import com.example.psrandroid.ui.screen.auth.LoginScreen
import com.example.psrandroid.ui.screen.auth.OTPScreen
import com.example.psrandroid.ui.screen.auth.PasswordScreen
import com.example.psrandroid.ui.screen.auth.SignupScreen
import com.example.psrandroid.ui.screen.dashboard.DashboardScreen
import com.example.psrandroid.ui.screen.dashboard.DashboardVM
import com.example.psrandroid.ui.screen.intro.SplashScreen
import com.example.psrandroid.ui.screen.profile.AddProfileScreen
import com.example.psrandroid.ui.screen.profile.ProfileVM

@Composable
fun PSRNavHost(
    navController: NavHostController,
) {
    val authViewModel: AuthVM = hiltViewModel()
    val profileVM: ProfileVM = hiltViewModel()
    val dashboardVM: DashboardVM = hiltViewModel()
    NavHost(navController, startDestination = Screen.SplashScreen.route) {
        composable(
            route = Screen.SplashScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            SplashScreen(navController, authViewModel)
        }

        composable(
            route = Screen.LanguagesScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            LanguageScreen(navController)
        }
        composable(
            route = Screen.CityScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            CityScreen(navController)
        }
        composable(
            route = Screen.LoginScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            LoginScreen(navController, authViewModel)
        }
        composable(
            route = Screen.OTPScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            OTPScreen(navController, authViewModel)
        }
        composable(
            route = Screen.RegisterScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            SignupScreen(navController, authViewModel)
        }
        composable(
            route = Screen.AddProfileScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            AddProfileScreen(navController, profileVM)
        }
        composable(
            route = Screen.DashBoardScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            DashboardScreen(navController, dashboardVM)
        }
        composable(
            route = Screen.PasswordScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            PasswordScreen(navController)
        }
    }
}