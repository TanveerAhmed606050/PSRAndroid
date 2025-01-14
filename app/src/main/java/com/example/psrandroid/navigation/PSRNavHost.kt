package com.example.psrandroid.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import com.example.psp_android.R
import com.example.psrandroid.ui.screen.auth.AuthVM
import com.example.psrandroid.ui.screen.auth.CityScreen
import com.example.psrandroid.ui.screen.auth.LanguageScreen
import com.example.psrandroid.ui.screen.auth.LoginScreen
import com.example.psrandroid.ui.screen.auth.OTPScreen
import com.example.psrandroid.ui.screen.auth.PasswordScreen
import com.example.psrandroid.ui.screen.auth.SignupScreen
import com.example.psrandroid.ui.screen.adPost.AdScreen
import com.example.psrandroid.ui.screen.adPost.DetailAdScreen
import com.example.psrandroid.ui.screen.adPost.AdPostScreen
import com.example.psrandroid.ui.screen.adPost.AdPostVM
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
            R.drawable.home_ic
        ),
        BottomNavigationItem(
            stringResource(id = R.string.rate),
            Screen.RateScreen.route,
            R.drawable.calendar_ic
        ),
        BottomNavigationItem(
            stringResource(id = R.string.ad_post),
            Screen.AdPostScreen.route,
            R.drawable.plus_bottom
        ),
        BottomNavigationItem(
            stringResource(id = R.string.prime_user),
            Screen.PrimeUserScreen.route,
            R.drawable.baseline_phone_24
        ),
        BottomNavigationItem(
            stringResource(id = R.string.lme),
            Screen.LmeScreen.route,
            R.drawable.baseline_location_pin_24
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
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            PrivacyPolicyScreen(navController)
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
            UpdateProfileScreen(navController, authViewModel)
        }
        composable(
            route = Screen.RateScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            RateScreen(navController, rateVM, authViewModel)
        }
        composable(
            route = Screen.PrimeUserScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            PrimeUserScreen(navController = navController, rateVM)
        }
        composable(
            route = Screen.PasswordScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            PasswordScreen(navController)
        }
        composable(
            route = Screen.MyProfileScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            MyProfileScreen(navController, authViewModel)
        }
        composable(
            route = Screen.UpdatePasswordScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            UpdatePasswordScreen(navController)
        }
        composable(
            route = Screen.LmeScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            LmeScreen(navController, rateVM)
        }
        composable(
            route = Screen.AdPostScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            AdPostScreen(navController, adPostVM)
        }
        composable(
            route = Screen.HomeScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            HomeScreen(navController, homeVM)
        }
        composable(
            route = Screen.AdScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            AdScreen(navController, adPostVM, rateVM = rateVM)
        }
        composable(
            route = Screen.AdDetailScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            DetailAdScreen(navController, adPostVM)
        }
    }
}