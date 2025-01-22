package com.example.psrandroid.ui.screen.profile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.psp_android.R
import com.example.psrandroid.dto.UserCredential
import com.example.psrandroid.network.isNetworkAvailable
import com.example.psrandroid.response.LocationData
import com.example.psrandroid.response.mockup
import com.example.psrandroid.ui.commonViews.AppButton
import com.example.psrandroid.ui.commonViews.Header
import com.example.psrandroid.ui.commonViews.ProfileInputField
import com.example.psrandroid.ui.screen.auth.AuthVM
import com.example.psrandroid.ui.screen.auth.ListDialog
import com.example.psrandroid.ui.theme.AppBG
import com.example.psrandroid.ui.theme.LightBlue
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.ui.theme.regularFont
import com.example.psrandroid.utils.Constant
import com.example.psrandroid.utils.isVisible
import com.example.psrandroid.utils.progressBar
import es.dmoral.toasty.Toasty
import io.github.rupinderjeet.kprogresshud.KProgressHUD

@Composable
fun UpdateProfileScreen(navController: NavController, authVM: AuthVM) {
    val sharedPreferences = authVM.userPreferences
    val context = LocalContext.current
    val progressBar: KProgressHUD = remember { context.progressBar() }
    progressBar.isVisible(authVM.isLoading)
    val userData = authVM.loginData
    if (userData != null) {
        if (userData.status) {
            Toasty.success(
                context,
                authVM.loginData?.message ?: "",
                Toast.LENGTH_SHORT,
                true
            )
                .show()
            navController.popBackStack()
        } else
            Toasty.error(
                context,
                authVM.loginData?.message ?: "",
                Toast.LENGTH_SHORT,
                true
            )
                .show()
        authVM.loginData = null
    }

    var name by remember {
        mutableStateOf(
            sharedPreferences.getUserPreference()?.name ?: ""
        )
    }
    var phone by remember {
        mutableStateOf(
            sharedPreferences.getUserPreference()?.phone ?: ""
        )
    }
    var address by remember {
        mutableStateOf(
            sharedPreferences.getUserPreference()?.location ?: ""
        )
    }
    val userId = sharedPreferences.getUserPreference()?.id ?: 0
    val locationList = authVM.userPreferences.getLocationList()?.data ?: listOf()

    val capturedImageUri by remember {
        mutableStateOf(
            sharedPreferences.getUserPreference()?.profilePic ?: ""
        )
    }

    UpdateProfileScreen(locationList, name, address, phone, capturedImageUri,
        onAddress = {
            address = it
        }, onName = {
            name = it
        }, onPhone = {
            phone = it
        },
        backClick = { navController.popBackStack() },
        updateButtonClick = {
            if (address.isEmpty())
                address = sharedPreferences.getUserPreference()?.location ?: ""
            if (name.isEmpty())
                name = sharedPreferences.getUserPreference()?.name ?: ""
//            if (phone.isEmpty())
//                phone = sharedPreferences.getUserPreference()?.phone ?: ""
            if (isNetworkAvailable(context))
                authVM.updateUserData(
                    UserCredential(
                        userId = "$userId",
                        phone = phone, name = name,
                        location = address
                    )
                )
            else
                Toasty.error(
                    context,
                    "No internet connection. Please check your network settings.",
                    Toast.LENGTH_SHORT,
                    true
                )
                    .show()
//            val loginData = User(name = name, phone = phone, userId = userId)
//            sharedPreferences.saveUserPreference(loginData)
//            navController.popBackStack()
        },
        onCitySelect = {
            address = it
        })
}

@Composable
fun UpdateProfileScreen(
    locationList: List<LocationData>,
    name: String, address: String, phone: String, imageUri: String,
    onName: (String) -> Unit, onPhone: (String) -> Unit,
    onAddress: (String) -> Unit,
    backClick: () -> Unit,
    updateButtonClick: () -> Unit,
    onCitySelect: (String) -> Unit,
) {
    var expandedCity by remember { mutableStateOf(false) }
    val locationData = locationList.map { it.name }
//    var city by remember { mutableStateOf("City") }

    if (expandedCity) {
        ListDialog(dataList = locationData, onDismiss = { expandedCity = false },
            onConfirm = { locationName ->
                val locationId = locationList.find { it.name == locationName }?.name ?: ""
                onCitySelect(locationId)
                onAddress(locationName)
                expandedCity = false
            })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBG)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.statusBarsPadding())
            Header(
                modifier = null,
                stringResource(id = R.string.update_profile),
                backClick = { backClick() })
            Spacer(modifier = Modifier.height(30.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                AsyncImage(
                    model = Constant.MEDIA_BASE_URL + imageUri,
                    placeholder = painterResource(id = R.drawable.user_placeholder),
                    error = painterResource(id = R.drawable.user_placeholder),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(130.dp)
                        .clip(shape = CircleShape)
                        .border(1.dp, Color.LightGray, CircleShape)
                )
                Spacer(modifier = Modifier.height(30.dp))
                ProfileInputField(
                    name,
                    KeyboardType.Text,
                    ImeAction.Next,
                    onValueChange = { value ->
                        onName(value)
                    },
                    icon = Icons.Default.Person,
                    placeholder = stringResource(id = R.string.name)
                )
                Spacer(modifier = Modifier.height(12.dp))
                ProfileInputField(
                    phone,
                    KeyboardType.Number,
                    ImeAction.Next,
                    onValueChange = { value ->
                        onPhone(value)
                    },
                    icon = Icons.Default.Phone,
                    placeholder = stringResource(id = R.string.phone),
                    enabled = false
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedCity = true }
                        .height(60.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(LightBlue, Color.White)
                            ),
                            shape = RoundedCornerShape(50) // Rounded corners for background
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_location_pin_24),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = 12.dp),
                        colorFilter = ColorFilter.tint(Color.White),
                    )
                    Text(
                        text = address,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = regularFont,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
                //                ProfileInputField(
//                    address,
//                    KeyboardType.Text,
//                    ImeAction.Done,
//                    onValueChange = { value ->
//                        onAddress(value)
//                    },
//                    icon = Icons.Default.LocationOn,
//                    placeholder = stringResource(id = R.string.address)
//                )
                Spacer(modifier = Modifier.height(32.dp))
                // Add button
//                Button(
//                    onClick = { updateButtonClick() },
//                    modifier = Modifier
//                        .fillMaxWidth(0.8f)
//                        .height(48.dp)
//                        .padding(horizontal = 24.dp),
//                    shape = RoundedCornerShape(16.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
//                ) {
//                    Text(
//                        text = stringResource(id = R.string.update_profile),
//                        color = LightBlue,
//                        style = MaterialTheme.typography.bodyLarge
//                    )
//                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .wrapContentHeight(Alignment.Bottom)
                ) {
                    AppButton(modifier = Modifier,
                        text = stringResource(id = R.string.update_profile),
                        onButtonClick = { updateButtonClick() })
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
        /*
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Header(
                modifier = null,
                stringResource(id = R.string.add_profile),
                backClick = { backClick() })
            Spacer(modifier = Modifier.height(50.dp))
            AsyncImage(
                model = imageUri,
                placeholder = painterResource(id = R.drawable.user_placeholder),
                error = painterResource(id = R.drawable.user_placeholder),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(100.dp)
                    .clip(shape = CircleShape)
                    .border(1.dp, Color.LightGray, CircleShape)
                    .clickable { onImageClick() }
            )
            Spacer(modifier = Modifier.height(50.dp))
// Input fields
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                ProfileInputField(
                    name,
                    KeyboardType.Text,
                    ImeAction.Next,
                    onValueChange = { value ->
                        onName(value)
                    },
                    icon = Icons.Default.Person,
                    placeholder = stringResource(id = R.string.name)
                )
                ProfileInputField(
                    email,
                    KeyboardType.Email,
                    ImeAction.Next,
                    onValueChange = { value ->
                        onEmail(value)
                    },
                    icon = Icons.Default.Email,
                    placeholder = stringResource(id = R.string.email)
                )
                ProfileInputField(
                    phone,
                    KeyboardType.Number,
                    ImeAction.Next,
                    onValueChange = { value ->
                        onPhone(value)
                    },
                    icon = Icons.Default.Phone,
                    placeholder = stringResource(id = R.string.phone)
                )
                ProfileInputField(
                    address,
                    KeyboardType.Text,
                    ImeAction.Done,
                    onValueChange = { value ->
                        onAddress(value)
                    },
                    icon = Icons.Default.LocationOn,
                    placeholder = stringResource(id = R.string.address)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

            // Add button
            Button(
                onClick = { addButtonClick() },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(48.dp)
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(
                    text = stringResource(id = R.string.add),
                    color = LightBlue,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

        }*/
    }
}

fun openGallery(
    context: Context,
    galleryLauncher: ManagedActivityResultLauncher<String, Uri?>,
    permissionLauncher: ManagedActivityResultLauncher<String, Uri?>
) {
    val permissionCheckResult =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
        galleryLauncher.launch("image/*")
    } else {
        // Request a permission
        permissionLauncher.launch("image/*")
    }
}

@Preview
@Composable
fun PreviewAddProfileScreen() {
    PSP_AndroidTheme {
        UpdateProfileScreen(
            listOf(LocationData.mockup),
            "",
            "",
            "",
            "",
            onPhone = {},
            onName = {},
            onAddress = {},
            backClick = {},
            updateButtonClick = {},
            onCitySelect = {})
    }
}
