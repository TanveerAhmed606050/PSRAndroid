package com.example.psrandroid.navigation

sealed class Screen(val route: String){
    data object SplashScreen : Screen("splashScreen")
    data object LanguagesScreen : Screen("LanguageScreen")
    data object CityScreen : Screen("CityScreen")
    data object LoginScreen : Screen("loginScreen")
    data object OTPScreen : Screen("otpScreen")
    data object RegisterScreen : Screen("RegisterScreen")
    data object PasswordScreen : Screen("passwordScreen")
    data object AddProfileScreen : Screen("addProfileScreen")
    data object DashBoardScreen : Screen("dashboardScreen")
}