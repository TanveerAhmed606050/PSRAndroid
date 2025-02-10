package com.example.psrandroid.navigation

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import com.example.psp_android.R
import com.example.psrandroid.ui.screen.adPost.AdPostVM
import com.example.psrandroid.ui.screen.adPost.AllPostScreen
import com.example.psrandroid.ui.screen.adPost.CreateAdScreen
import com.example.psrandroid.ui.screen.adPost.DetailAdScreen
import com.example.psrandroid.ui.screen.adPost.MyPostScreen
import com.example.psrandroid.ui.screen.adPost.models.AdData
import com.example.psrandroid.ui.screen.auth.AuthVM
import com.example.psrandroid.ui.screen.auth.ForgotPasswordScreen
import com.example.psrandroid.ui.screen.auth.LanguageScreen
import com.example.psrandroid.ui.screen.auth.LoginScreen
import com.example.psrandroid.ui.screen.auth.PasswordScreen
import com.example.psrandroid.ui.screen.auth.ResetPasswordScreen
import com.example.psrandroid.ui.screen.auth.SignupScreen
import com.example.psrandroid.ui.screen.home.HomeScreen
import com.example.psrandroid.ui.screen.home.HomeVM
import com.example.psrandroid.ui.screen.intro.PrivacyPolicyScreen
import com.example.psrandroid.ui.screen.intro.SplashScreen
import com.example.psrandroid.ui.screen.lme.LmeScreen
import com.example.psrandroid.ui.screen.lme.PrimeUserScreen
import com.example.psrandroid.ui.screen.profile.MyProfileScreen
import com.example.psrandroid.ui.screen.profile.ProfileVM
import com.example.psrandroid.ui.screen.profile.UpdatePasswordScreen
import com.example.psrandroid.ui.screen.profile.UpdateProfileScreen
import com.example.psrandroid.ui.screen.rate.RateScreen
import com.example.psrandroid.ui.screen.rate.RateVM
import com.example.psrandroid.utils.DevicePosture
import com.example.psrandroid.utils.PSRContentType
import com.example.psrandroid.utils.PSRNavigationContentPosition
import com.example.psrandroid.utils.PSRNavigationType
import com.example.psrandroid.utils.isBookPosture
import com.example.psrandroid.utils.isSeparating
import com.google.gson.Gson

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PSRNavigation(
    navController: NavHostController,
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
    onLogOut: () -> Unit
) {
    val navigationType: PSRNavigationType
    val contentType: PSRContentType
    val foldingFeature = displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()
    val foldingDevicePosture = when {
        isBookPosture(foldingFeature) ->
            DevicePosture.BookPosture(foldingFeature.bounds)

        isSeparating(foldingFeature) ->
            DevicePosture.Separating(foldingFeature.bounds, foldingFeature.orientation)

        else -> DevicePosture.NormalPosture
    }
    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            navigationType = PSRNavigationType.BOTTOM_NAVIGATION
            contentType = PSRContentType.SINGLE_PANE
        }

        WindowWidthSizeClass.Medium -> {
            navigationType = PSRNavigationType.NAVIGATION_RAIL
            contentType = if (foldingDevicePosture != DevicePosture.NormalPosture) {
                PSRContentType.DUAL_PANE
            } else {
                PSRContentType.SINGLE_PANE
            }
        }

        WindowWidthSizeClass.Expanded -> {
            navigationType = if (foldingDevicePosture is DevicePosture.BookPosture) {
                PSRNavigationType.NAVIGATION_RAIL
            } else {
                PSRNavigationType.PERMANENT_NAVIGATION_DRAWER
            }
            contentType = PSRContentType.DUAL_PANE
        }

        else -> {
            navigationType = PSRNavigationType.BOTTOM_NAVIGATION
            contentType = PSRContentType.SINGLE_PANE
        }
    }

    val navigationContentPosition = when (windowSize.heightSizeClass) {
        WindowHeightSizeClass.Compact -> {
            PSRNavigationContentPosition.TOP
        }

        WindowHeightSizeClass.Medium,
        WindowHeightSizeClass.Expanded -> {
            PSRNavigationContentPosition.CENTER
        }

        else -> {
            PSRNavigationContentPosition.TOP
        }
    }

    val bottomMenuList = listOf(
        BottomNavigationItem(
            stringResource(id = R.string.home),
            Screen.HomeScreen.route,
            R.drawable.home_ic,

        ),
        BottomNavigationItem(
            stringResource(id = R.string.rate),
            Screen.RateScreen.route,
            R.drawable.calendar_ic,
        ),
        BottomNavigationItem(
            stringResource(id = R.string.post),
            Screen.MyPostScreen.route,
            R.drawable.calendar_ic,
        ),
        BottomNavigationItem(
            stringResource(id = R.string.prime_user),
            Screen.PrimeUserScreen.route,
            R.drawable.baseline_phone_24,
        ),
        BottomNavigationItem(
            stringResource(id = R.string.lme),
            Screen.LmeScreen.route,
            R.drawable.baseline_location_pin_24,

        ),
        BottomNavigationItem(
            stringResource(id = R.string.profile),
            Screen.MyProfileScreen.route,
            R.drawable.home_ic,

        ),
    )

    PSRNavigationWrapper(
        navController = navController,
        bottomMenuList = bottomMenuList,
        navigationType = navigationType,
        contentType = contentType,
        displayFeatures = displayFeatures,
        navigationContentPosition = navigationContentPosition,
        onLogOut = { onLogOut() }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PSRNavigationWrapper(
    navController: NavHostController,
    bottomMenuList: List<BottomNavigationItem>,
    navigationType: PSRNavigationType,
    contentType: PSRContentType,
    displayFeatures: List<DisplayFeature>,
    navigationContentPosition: PSRNavigationContentPosition,
    onLogOut: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navigationActions = remember(navController) {
        PSRNavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination = navBackStackEntry?.destination?.route ?: Screen.HomeScreen.route
    if (navigationType == PSRNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        // TODO check on custom width of PermanentNavigationDrawer: b/232495216
        PermanentNavigationDrawer(drawerContent = {
            if (bottomMenuList.any { it.route == navController.currentDestination?.route }) PermanentNavigationDrawerContent(
                bottomMenuList = bottomMenuList,
                selectedDestination = selectedDestination,
                navigationContentPosition = navigationContentPosition,
                navigateToTopLevelDestination = navigationActions::navigateTo,
                onLogOut = { onLogOut() }
            )
        }) {
            PSRAppContent(
                bottomMenuList = bottomMenuList,
                navigationType = navigationType,
                contentType = contentType,
                displayFeatures = displayFeatures,
                navigationContentPosition = navigationContentPosition,
                navController = navController,
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigationActions::navigateTo,
                onLogOut = { onLogOut() }
            )
        }
    } else {
//        ModalNavigationDrawer(
//            drawerState = drawerState,
//            drawerContent = {
//                if (bottomMenuList.any { it.route == navController.currentDestination?.route }) ModalNavigationDrawerContent(
//                    bottomMenuList = bottomMenuList,
//                    selectedDestination = selectedDestination,
//                    navigationContentPosition = navigationContentPosition,
//                    navigateToTopLevelDestination = navigationActions::navigateTo,
//                    onDrawerClicked = {
//                        scope.launch {
//                            drawerState.close()
//                        }
//                    },
//                    onLogOut = { onLogOut() }
//                )
//            },
//        ) {
        PSRAppContent(
            bottomMenuList = bottomMenuList,
            navigationType = navigationType,
            contentType = contentType,
            displayFeatures = displayFeatures,
            navigationContentPosition = navigationContentPosition,
            navController = navController,
            selectedDestination = selectedDestination,
            navigateToTopLevelDestination = navigationActions::navigateTo
        ) {}
//        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PSRAppContent(
    modifier: Modifier = Modifier,
    bottomMenuList: List<BottomNavigationItem>,
    navigationType: PSRNavigationType,
    contentType: PSRContentType,
    displayFeatures: List<DisplayFeature>,
    navigationContentPosition: PSRNavigationContentPosition,
    navController: NavHostController,
    selectedDestination: String,
    navigateToTopLevelDestination: (BottomNavigationItem) -> Unit,
    onDrawerClicked: () -> Unit = {},
    onLogOut: () -> Unit
) {
    Row(modifier = modifier.fillMaxSize()) {
        if (bottomMenuList.any { it.route == navController.currentDestination?.route }) AnimatedVisibility(
            visible = navigationType == PSRNavigationType.NAVIGATION_RAIL
        ) {
//            PSRNavigationRail(
//                bottomMenuList = bottomMenuList,
//                selectedDestination = selectedDestination,
//                navigationContentPosition = navigationContentPosition,
//                navigateToTopLevelDestination = navigateToTopLevelDestination,
//                onDrawerClicked = onDrawerClicked,
//                onLogOut = { onLogOut() }
//            )
        }
        Scaffold(
            bottomBar = {
                if (bottomMenuList.any { it.route == navController.currentDestination?.route }) {
                    AnimatedVisibility(visible = navigationType == PSRNavigationType.BOTTOM_NAVIGATION) {
                        PSRBottomNavigationBar(
                            bottomMenuList = bottomMenuList,
                            selectedDestination = selectedDestination,
                            navigateToTopLevelDestination = navigateToTopLevelDestination
                        )
                    }
                }
            }
        ) { innerPadding ->
            PSRNavHost(
                navController = navController,
                modifier = Modifier
                    .padding(innerPadding)
                    .weight(1f),
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PSRNavHost(
    navController: NavHostController,
    modifier: Modifier
) {
    val authViewModel: AuthVM = hiltViewModel()
    val profileVM: ProfileVM = hiltViewModel()
    val rateVM: RateVM = hiltViewModel()
    val adPostVM: AdPostVM = hiltViewModel()
    val homeVM: HomeVM = hiltViewModel()
    NavHost(navController, startDestination = Screen.SplashScreen.route) {
        composable(
            route = Screen.SplashScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            SplashScreen(navController, authViewModel)
        }

        composable(
            route = Screen.PrivacyPolicyScreen.route,
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            PrivacyPolicyScreen(navController, authViewModel)
        }

        composable(
            route = Screen.LanguagesScreen.route,
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            LanguageScreen(navController, homeVM)
        }
        composable(
            route = Screen.LoginScreen.route,
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            LoginScreen(navController, authViewModel)
        }
        composable(
            route = Screen.RegisterScreen.route,
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            SignupScreen(navController, authViewModel)
        }
        composable(
            route = Screen.AddProfileScreen.route,
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            UpdateProfileScreen(navController, authViewModel)
        }
        composable(
            route = Screen.RateScreen.route,
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            RateScreen(rateVM)
        }
        composable(
            route = Screen.PrimeUserScreen.route,
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            PrimeUserScreen(rateVM)
        }
        composable(
            route = Screen.PasswordScreen.route,
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            PasswordScreen(navController)
        }
        composable(
            route = Screen.MyProfileScreen.route,
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            MyProfileScreen(navController, authViewModel)
        }
        composable(
            route = Screen.ForgotPasswordScreen.route,
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            ForgotPasswordScreen(navController, authViewModel)
        }
        composable(
            route = Screen.ResetPasswordScreen.route,
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            ResetPasswordScreen(navController, authViewModel)
        }
        composable(
            route = Screen.UpdatePasswordScreen.route,
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            UpdatePasswordScreen(navController, profileVM)
        }
        composable(
            route = Screen.LmeScreen.route,
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            LmeScreen(rateVM)
        }
        composable(
            route = Screen.MyPostScreen.route,
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            MyPostScreen(navController, adPostVM)
        }
        composable(
            route = Screen.AllPostScreen.route,
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            AllPostScreen(navController, adPostVM)
        }
        composable(
            route = Screen.HomeScreen.route,
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            HomeScreen(navController, homeVM)
        }
        composable(
            route = Screen.AdScreen.route,
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            CreateAdScreen(navController, adPostVM, rateVM = rateVM)
        }
        composable(
            route = Screen.AdDetailScreen.route + "Details/{data}",
            arguments = listOf(navArgument("data") { type = NavType.StringType }),
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) { backStackEntry ->
            val encodedJson = backStackEntry.arguments?.getString("data")
            val userJson = encodedJson?.let { Uri.decode(it) }
            val adData = userJson?.let { Gson().fromJson(it, AdData::class.java) }
            DetailAdScreen(navController, rateVM, adData)
        }
    }
}