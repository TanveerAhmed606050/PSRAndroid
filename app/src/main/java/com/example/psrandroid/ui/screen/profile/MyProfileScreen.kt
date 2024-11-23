package com.example.psrandroid.ui.screen.profile

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.psp_android.R
import com.example.psrandroid.dto.ImageUpdate
import com.example.psrandroid.dto.ProfileOption
import com.example.psrandroid.navigation.Screen
import com.example.psrandroid.network.isNetworkAvailable
import com.example.psrandroid.ui.commonViews.LogoutDialog
import com.example.psrandroid.ui.commonViews.MyAsyncImage
import com.example.psrandroid.ui.screen.auth.AuthVM
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.LightBlue
import com.example.psrandroid.ui.theme.mediumFont
import com.example.psrandroid.ui.theme.regularFont
import com.example.psrandroid.utils.LogoutSession
import com.example.psrandroid.utils.Utils.convertImageFileToBase64
import com.example.psrandroid.utils.isVisible
import com.example.psrandroid.utils.progressBar
import es.dmoral.toasty.Toasty
import io.github.rupinderjeet.kprogresshud.KProgressHUD

@Composable
fun MyProfileScreen(navController: NavController, authVM: AuthVM) {
    val context = LocalContext.current
    val userData = authVM.userPreferences.getUserPreference()
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var base64String by remember { mutableStateOf<String?>(null) }
    var isLogout by remember { mutableStateOf(false) }
    val userId = authVM.userPreferences.getUserPreference()?.id ?: 0
    val authData = authVM.loginData
    var profilePic = userData?.profilePic ?: ""
    // Display the auth data
    if (authData != null) {
        if (authData.status) {
            Toasty.success(context, authData.message, Toast.LENGTH_SHORT, true).show()
            profilePic = authData.data.profilePic ?: ""
        } else
            Toasty.error(context, authData.message, Toast.LENGTH_SHORT, true).show()
        authVM.loginData = null
    }
    //logout
    if (isLogout) LogoutDialog(onOkClick = {
        isLogout = false
        authVM.userPreferences.clearStorage()
        LogoutSession.clearError()
        navController.navigate(Screen.LoginScreen.route){
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    },
        onDismissRequest = {
            isLogout = false
        })
    val progressBar: KProgressHUD = remember { context.progressBar() }
    progressBar.isVisible(authVM.isLoading)
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
//        base64String =
//            capturedImageUri?.let {
//                convertImageFileToBase64(
//                    it,
//                    contentResolver = context.contentResolver
//                )
//            }
    }
    val galleryLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        )
        { uri: Uri? ->
            capturedImageUri = uri
        }

    MyProfileScreen(profilePic = profilePic, onBottomSheetShow = {
        openGallery(context, galleryLauncher, pickImageLauncher)
    },
        onItemClick = { screenRoute ->
            if (screenRoute == "Logout")
                isLogout = true
            else
                navController.navigate(screenRoute)
        })
}

@Composable
fun MyProfileScreen(
    profilePic: String,
    onItemClick: (String) -> Unit,
    onBottomSheetShow: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(LightBlue, DarkBlue)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 0.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.statusBarsPadding())
            Text(
                text = stringResource(id = R.string.my_profile), fontSize = 16.sp,
                fontFamily = mediumFont,
                color = Color.White
            )
            Spacer(modifier = Modifier.padding(top = 20.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box {
                    MyAsyncImage(imageUrl = profilePic, 50.dp, true)
                    Surface(
                        color = LightBlue,
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
            ProfileOptions(screenRoute = { route ->
                onItemClick(route)
            })
        }
    }
}

@Composable
fun ProfileOptions(
    screenRoute: (String) -> Unit
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
            R.drawable.baseline_privacy_tip_24,
            stringResource(id = R.string.privacy_policy),
        ),
        ProfileOption(
            R.drawable.baseline_lock_24,
            stringResource(id = R.string.terms_condition),
        ),
        ProfileOption(
            R.drawable.baseline_lock_24,
            stringResource(id = R.string.contact_us)
        ),
        ProfileOption(
            R.drawable.baseline_lock_24,
            stringResource(id = R.string.logout)
        ),
    )
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color = Color.White)
    ) {
        options.forEach { option ->
            ProfileOptionItem(option, screenRoute)
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = colorResource(id = R.color.text_grey)
            )
        }
    }
}

@Composable
fun ProfileOptionItem(
    option: ProfileOption,
    screenRoute: (String) -> Unit
) {
    val context = LocalContext.current
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
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 8.dp)
            .clickable {
                if (encodedUrl.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(encodedUrl))
                    context.startActivity(intent)
                } else if (route.isNotEmpty())
                    screenRoute(route)
                else if (route.isEmpty())
                    screenRoute("Logout")
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(option.iconId), contentDescription = null
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = option.title,
                fontSize = 16.sp,
                fontFamily = regularFont,
                color = option.color
            )
        }
        Image(painter = painterResource(id = R.drawable.next_blk), contentDescription = null)
    }
}

@Preview
@Composable
fun MyProfileScreenPreview() {
    MyProfileScreen(
        onBottomSheetShow = {},
        profilePic = "",
        onItemClick = {}
    )
}