package com.example.psrandroid.ui.screen.profile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.psp_android.R
import com.example.psrandroid.response.User
import com.example.psrandroid.ui.commonViews.ProfileInputField
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.LightBlue
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.utils.Utils.convertImageFileToBase64

@Composable
fun AddProfileScreen(navController: NavController, profileVM: ProfileVM) {
    val context = LocalContext.current
    val sharedPreferences = profileVM.userPreferences

    var name by rememberSaveable {
        mutableStateOf(
            sharedPreferences.getUserPreference()?.name ?: ""
        )
    }
    var email by rememberSaveable {
        mutableStateOf(
            sharedPreferences.getUserPreference()?.email ?: ""
        )
    }
    var phone by rememberSaveable {
        mutableStateOf(
            sharedPreferences.getUserPreference()?.phone ?: ""
        )
    }
    var address by rememberSaveable {
        mutableStateOf(""
        )
    }
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var base64String by remember { mutableStateOf<String?>(null) }

    val galleryLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            capturedImageUri = uri
        }
    val pickImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        capturedImageUri = uri
        base64String =
            capturedImageUri?.let {
                convertImageFileToBase64(
                    it,
                    contentResolver = context.contentResolver
                )
            }
        //call update api
        if (base64String != null) {
//            val updateProfilePicture =
//                UpdateProfilePicture("${userData?.id}", base64String ?: "")
//            profileViewModel.editProfilePicture(updateProfilePicture)
        }
    }

    AddProfileScreen(name, email, address, phone, capturedImageUri,
        onAddress = {
            address = it
        }, onName = {
            name = it
        }, onEmail = {
            email = it
        }, onPhone = {
            phone = it
        },
        backClick = { navController.popBackStack() }, addButtonClick = {
            val loginData = User(name = name, email = email, phone = phone)
            sharedPreferences.saveUserPreference(loginData)
            navController.popBackStack()
        },
        onImageClick = {
            openGallery(
                context, galleryLauncher = galleryLauncher,
                pickImageLauncher
            )
        })
}

@Composable
fun AddProfileScreen(
    name: String, email: String, address: String, phone: String, imageUri: Uri?,
    onName: (String) -> Unit, onEmail: (String) -> Unit, onPhone: (String) -> Unit,
    onAddress: (String) -> Unit,
    backClick: () -> Unit, addButtonClick: () -> Unit,
    onImageClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(LightBlue, DarkBlue)))
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = stringResource(id = R.string.add_profile),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(50.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = imageUri,
                    placeholder = painterResource(id = R.drawable.user_placeholder),
                    error = painterResource(id = R.drawable.user_placeholder),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(130.dp)
                        .clip(shape = CircleShape)
                        .border(1.dp, Color.LightGray, CircleShape)
                        .clickable { onImageClick() }
                )
                Spacer(modifier = Modifier.height(50.dp))
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
                    email,
                    KeyboardType.Email,
                    ImeAction.Next,
                    onValueChange = { value ->
                        onEmail(value)
                    },
                    icon = Icons.Default.Email,
                    placeholder = stringResource(id = R.string.email)
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
                    placeholder = stringResource(id = R.string.phone)
                )
                Spacer(modifier = Modifier.height(12.dp))
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
                Spacer(modifier = Modifier.height(32.dp))
                // Add button
                Button(
                    onClick = { addButtonClick() },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(48.dp)
                        .padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(
                        text = stringResource(id = R.string.add),
                        color = LightBlue,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
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
        ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
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
        AddProfileScreen(
            "",
            "",
            "",
            "",
            null,
            onPhone = {},
            onName = {},
            onEmail = {},
            onAddress = {},
            backClick = {},
            addButtonClick = {},
            onImageClick = {})
    }
}
