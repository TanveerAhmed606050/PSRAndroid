package com.example.psrandroid.ui.screen.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.psp_android.R
import com.example.psrandroid.dto.ImageUpdate
import com.example.psrandroid.dto.ProfileOption
import com.example.psrandroid.navigation.Screen
import com.example.psrandroid.network.isNetworkAvailable
import com.example.psrandroid.ui.commonViews.Header
import com.example.psrandroid.ui.commonViews.LoadingDialog
import com.example.psrandroid.ui.commonViews.LogoutDialog
import com.example.psrandroid.ui.commonViews.MyAsyncImage
import com.example.psrandroid.ui.screen.auth.AuthVM
import com.example.psrandroid.ui.screen.auth.ListDialog
import com.example.psrandroid.ui.theme.AppBG
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.LightBlue
import com.example.psrandroid.ui.theme.regularFont
import com.example.psrandroid.utils.LogoutSession
import com.example.psrandroid.utils.Utils.convertImageFileToBase64
import com.example.psrandroid.utils.Utils.isRtlLocale
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import es.dmoral.toasty.Toasty
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MyProfileScreen(navController: NavController, authVM: AuthVM) {
    val context = LocalContext.current
    val userData = authVM.userPreferences.getUserPreference()
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var base64String by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var expandedLang by remember { mutableStateOf(false) }
    val languageList = listOf("اردو", "English")
    if (expandedLang) {
        ListDialog(dataList = languageList, onDismiss = { expandedLang = false },
            onConfirm = { selectedLanguage ->
                authVM.userPreferences.isUrduSelected = selectedLanguage == "اردو"
                expandedLang = false
                if (authVM.userPreferences.isUrduSelected) {
                    switchLanguage(context, "ur")
                } else {
                    switchLanguage(context, "en")
                }
                (context as? Activity)?.recreate()
            })
    }
    val userId = authVM.userPreferences.getUserPreference()?.id ?: 0
    val authData = authVM.loginData
    var profilePic = userData?.profilePic ?: ""
    // Display the auth data
    if (authData != null) {
        if (authData.status) {
            Toasty.success(context, authData.message, Toast.LENGTH_SHORT, true).show()
            profilePic = authData.data.profilePic
        } else
            Toasty.error(context, authData.message, Toast.LENGTH_SHORT, true).show()
        authVM.loginData = null
    }

    if (showDialog)
        LogoutDialog(onOkClick = {
            showDialog = false
            authVM.userPreferences.clearStorage()
            LogoutSession.clearError()
            navController.navigate(Screen.LoginScreen.route) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        },
            onDismissRequest = {
                showDialog = false
            })
    var showProgress by remember { mutableStateOf(false) }
    showProgress = authVM.isLoading
    if (showProgress)
        LoadingDialog()
    val noInternetMessage = stringResource(id = R.string.network_error)

    val pickImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        capturedImageUri = uri
        if (capturedImageUri != null) {
            base64String = convertImageFileToBase64(capturedImageUri!!, context.contentResolver)
            authVM.updateUserImage(ImageUpdate(userId, image = base64String ?: ""))
            if (isNetworkAvailable(context)) {
                authVM.updateUserImage(ImageUpdate(userId, image = base64String ?: ""))
            } else
                Toasty.error(context, noInternetMessage, Toast.LENGTH_SHORT, true)
                    .show()
        }
    }
    val permissionState =
        rememberPermissionState(permission = "android.permission.POST_NOTIFICATIONS")

    val galleryLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        )
        { uri: Uri? ->
            capturedImageUri = uri
        }

    MyProfileScreen(
        context = context,
        profilePic = profilePic,
        onBottomSheetShow = {
            openGallery(context, galleryLauncher, pickImageLauncher)
        },
        onItemClick = { screenRoute ->
            when (screenRoute) {
                context.getString(R.string.logout) -> showDialog = true
                context.getString(R.string.selected_language) -> {
                    expandedLang = true
                }

                else -> navController.navigate(screenRoute)
            }
        },
        backClick = { navController.popBackStack() },
        onNotificationSelection = { isChecked ->
// Check if permission is granted
            Log.d("dkshg", "MyProfileScreen:$isChecked")
            if (!permissionState.status.isGranted) {
                permissionState.launchPermissionRequest()
            }
            authVM.userPreferences.notificationSwitchSelected = isChecked
        },
        isNotificationEnable = authVM.userPreferences.notificationSwitchSelected
    )
}

@Composable
fun MyProfileScreen(
    context: Context,
    isNotificationEnable: Boolean,
    profilePic: String,
    onItemClick: (String) -> Unit,
    onBottomSheetShow: () -> Unit,
    backClick: () -> Unit,
    onNotificationSelection: (Boolean) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBG)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.statusBarsPadding())
            Header(
                modifier = null,
                stringResource(id = R.string.my_profile),
                backClick = { backClick() })
            Spacer(modifier = Modifier.padding(top = 20.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box {
                    MyAsyncImage(imageUrl = profilePic, 50.dp, true)
                    Surface(
                        color = DarkBlue,
                        shape = CircleShape,
                        modifier = Modifier
                            .width(16.dp)
                            .height(16.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Image(
                                painter = painterResource(id = R.drawable.edit_white_ic),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.clickable { onBottomSheetShow() })
                        }
                    }
                }

            }
            Spacer(modifier = Modifier.height(20.dp))
            ProfileOptions(context, isChecked = isNotificationEnable, screenRoute = { route ->
                onItemClick(route)
            },
                onNotificationSelection = { isChecked ->
                    onNotificationSelection(isChecked)
                })
        }
    }
}

@Composable
fun ProfileOptions(
    context: Context,
    isChecked: Boolean,
    screenRoute: (String) -> Unit,
    onNotificationSelection: (Boolean) -> Unit,
) {
    val options = listOf(
        ProfileOption(
            R.drawable.baseline_lock_24,
            stringResource(id = R.string.update_profile),
        ),
        ProfileOption(
            R.drawable.baseline_lock_24,
            stringResource(id = R.string.password_change),
        ),
        ProfileOption(
            R.drawable.globe_ic,
            stringResource(id = R.string.selected_language)
        ),
        ProfileOption(
            R.drawable.notification_ic,
            stringResource(id = R.string.notification),
            switch = true,
        ),
        ProfileOption(
            R.drawable.baseline_privacy_tip_24,
            stringResource(id = R.string.privacy_policy),
        ),
        ProfileOption(
            R.drawable.terms_conditions,
            stringResource(id = R.string.terms_condition),
        ),
        ProfileOption(
            R.drawable.contact_us,
            stringResource(id = R.string.contact_us)
        ),
        ProfileOption(
            R.drawable.logout,
            stringResource(id = R.string.logout)
        ),
    )
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color = Color.White)
    ) {
        options.forEach { option ->
            ProfileOptionItem(context, isChecked, option, screenRoute, onNotificationSelection)
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = DarkBlue
            )
        }
    }
}

@Composable
fun ProfileOptionItem(
    context: Context,
    isSelected: Boolean,
    option: ProfileOption,
    screenRoute: (String) -> Unit,
    onNotificationSelection: (Boolean) -> Unit,
) {
    var isChecked by remember { mutableStateOf(isSelected) }
    val currentLocale = Locale.getDefault()
    val isRtl = isRtlLocale(currentLocale)
    var route = ""
    var encodedUrl = ""
    when (option.title) {
        stringResource(id = R.string.privacy_policy) -> {
            encodedUrl = "https://fitmeper.com/Privacy"
            route = ""
        }

        stringResource(id = R.string.terms_condition) -> {
            encodedUrl = "https://fitmepner.com/Terms"
            route = ""
        }

        stringResource(id = R.string.logout) -> {
            encodedUrl = ""
            route = stringResource(id = R.string.logout)
        }

        stringResource(id = R.string.contact_us) -> {
            encodedUrl = "https://fitmepner.com/Contact"
            route = ""
        }

        stringResource(id = R.string.update_profile) -> {
            route = Screen.AddProfileScreen.route
            encodedUrl = ""
        }

        stringResource(id = R.string.password_change) -> {
            route = Screen.UpdatePasswordScreen.route
            encodedUrl = ""
        }

        stringResource(id = R.string.selected_language) -> {
            route = stringResource(id = R.string.selected_language)
            encodedUrl = ""
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp, horizontal = 8.dp)
            .clickable {
                if (encodedUrl.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(encodedUrl))
                    context.startActivity(intent)
                } else if (route == context.getString(R.string.logout))
                    screenRoute(context.getString(R.string.logout))
                else if (route.isNotEmpty())
                    screenRoute(route)
//                else if (route.isEmpty())
//                    screenRoute(context.getString(R.string.logout))
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (option.switch && isRtl) {
            Switch(
                checked = isChecked, onCheckedChange = { checked ->
                    isChecked = checked
                    onNotificationSelection(checked)
                }, colors = SwitchDefaults.colors(
                    checkedThumbColor = DarkBlue,
                    uncheckedThumbColor = Color.Gray,
                    checkedTrackColor = DarkBlue.copy(alpha = 0.5f),
                    uncheckedTrackColor = Color.Gray.copy(alpha = 0.5f)
                )
            )
        } else {
            Image(
                painter = if (isRtl) painterResource(id = R.drawable.baseline_arrow_back_ios_24) else painterResource(
                    option.iconId
                ), contentDescription = null,
                colorFilter = ColorFilter.tint(LightBlue),
                modifier = Modifier.size(if (isRtl) 20.dp else 30.dp)
            )
        }
        if (isRtl)
            Spacer(modifier = Modifier.weight(1f))
        Text(
            text = option.title,
            fontSize = 16.sp,
            fontFamily = regularFont,
            color = option.color,
            textAlign = TextAlign.End,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
        if (!isRtl)
            Spacer(modifier = Modifier.weight(1f))
        if (option.switch && !isRtl) {
            Switch(
                checked = isChecked, onCheckedChange = { checked ->
                    isChecked = checked
                    onNotificationSelection(checked)
                }, colors = SwitchDefaults.colors(
                    checkedThumbColor = DarkBlue,
                    uncheckedThumbColor = Color.Gray,
                    checkedTrackColor = DarkBlue.copy(alpha = 0.5f),
                    uncheckedTrackColor = Color.Gray.copy(alpha = 0.5f)
                )
            )
        } else {
            Image(
                painter = if (isRtl) painterResource(option.iconId) else painterResource(id = R.drawable.next_blk),
                contentDescription = null,
                colorFilter = ColorFilter.tint(LightBlue),
                modifier = Modifier.size(if (isRtl) 30.dp else 20.dp)
            )
        }
    }
}

fun switchLanguage(context: Context, language: String) {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val configuration = context.resources.configuration
    configuration.setLocale(locale)
    context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
}

@Preview
@Composable
fun MyProfileScreenPreview() {
    MyProfileScreen(
        context = LocalContext.current,
        onBottomSheetShow = {},
        profilePic = "",
        onItemClick = {},
        backClick = {},
        onNotificationSelection = {},
        isNotificationEnable = false
    )
}