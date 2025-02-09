package com.example.psrandroid.ui.screen.adPost

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.psp_android.R
import com.example.psrandroid.network.isNetworkAvailable
import com.example.psrandroid.response.LocationData
import com.example.psrandroid.response.MetalData
import com.example.psrandroid.response.mockup
import com.example.psrandroid.ui.commonViews.AppButton
import com.example.psrandroid.ui.commonViews.CustomTextField
import com.example.psrandroid.ui.commonViews.FullScreenImageDialog
import com.example.psrandroid.ui.commonViews.Header
import com.example.psrandroid.ui.commonViews.LoadingDialog
import com.example.psrandroid.ui.commonViews.showRewardedAd
import com.example.psrandroid.ui.screen.adPost.models.CreatePost
import com.example.psrandroid.ui.screen.auth.ListDialog
import com.example.psrandroid.ui.screen.rate.RateVM
import com.example.psrandroid.ui.screen.rate.SearchBar
import com.example.psrandroid.ui.screen.rate.models.SubData
import com.example.psrandroid.ui.theme.AppBG
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.ui.theme.regularFont
import com.example.psrandroid.utils.Utils.convertImageFileToBase64
import com.example.psrandroid.utils.Utils.isRtlLocale
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.android.gms.ads.MobileAds
import es.dmoral.toasty.Toasty
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateAdScreen(navController: NavController, adPostVM: AdPostVM, rateVM: RateVM) {
    val context = LocalContext.current
    MobileAds.initialize(context) { initializationStatus ->
        Log.d("RewardedAd", "Mobile Ads initialized: $initializationStatus")
    }
    val locationList = adPostVM.userPreferences.getLocationList()?.data ?: listOf()
    var mainMetalName by remember { mutableStateOf(TextFieldValue("")) }
    var subMetalName by remember { mutableStateOf(TextFieldValue("")) }
    var selectedCity by remember { mutableStateOf("Lahore") }
    val suggestedSearchList = rateVM.suggestMainMetals
    val suggestedSubMetalList = adPostVM.suggestSubMetals
    val adPostResponse = adPostVM.adPostResponse
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val base64List = ArrayList<String>()
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val noInternetMessage = stringResource(id = R.string.network_error)
// Display the error message
    if (errorMessage != null) {
        Toasty.error(context, errorMessage ?: "", Toast.LENGTH_SHORT, true)
            .show()
    }
    if (adPostResponse != null) {
        if (adPostResponse.status)
            Toasty.success(context, adPostResponse.message, Toast.LENGTH_SHORT, true)
                .show()
        else
            Toasty.error(context, adPostResponse.message, Toast.LENGTH_SHORT, true)
                .show()
        navController.popBackStack()
        adPostVM.adPostResponse = null
    }
    val locationId = rateVM.userPreferences.getLocationList()?.data?.find {
        it.name.equals(rateVM.userPreferences.getUserPreference()?.location, ignoreCase = true)
    }?.id ?: 0 // get location id
    if (isNetworkAvailable(context)) {
        LaunchedEffect(key1 = Unit) {
            adPostVM.getAllSubMetals()
            rateVM.getMainMetals("$locationId", "")
        }
    } else
        Toasty.error(context, noInternetMessage, Toast.LENGTH_SHORT, true)
            .show()

    var showDialog by remember { mutableStateOf(false) }
    if (showDialog)
        FullScreenImageDialog(imageList = selectedImages, onDismissRequest = {
            showDialog = false
        }, serverImageList = null)
    var showProgress by remember { mutableStateOf(false) }
    showProgress = adPostVM.isLoading
    if (showProgress)
        LoadingDialog()

    // Launcher for selecting multiple images
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        val maxFileSizeMb = 5
        val maxFileSizeBytes = maxFileSizeMb * 1024 * 1024  // Convert MB to bytes

        val totalSize = uris.sumOf { uri ->
            getFileSizeFromUri(context, uri)
        }
        if (totalSize <= maxFileSizeBytes) {
            selectedImages = uris.take(3) // Limit to the first 3 images
            for (uri in selectedImages) {
                val base64String = convertImageFileToBase64(uri, context.contentResolver)
                base64List.add(base64String)
            }
            errorMessage = null
        } else {
            selectedImages = emptyList() // Clear selection
            errorMessage = "Total file size exceeds $maxFileSizeMb MB. Please select smaller files."
        }
    }

    AdScreenView(
        selectedImages,
        locationList = locationList,
        subMetalSearch = subMetalName,
        search = mainMetalName,
        selectedCity = selectedCity,
        suggestedSearchList = suggestedSearchList,
        suggestedSubMetalList = suggestedSubMetalList,
        onBackClick = { navController.popBackStack() },
        onSearch = {
            mainMetalName = it
            rateVM.searchMainMetals(mainMetalName.text)
        },
        onSearchClick = {
            mainMetalName = it
            rateVM.searchMainMetals(mainMetalName.text)
        },
        onSubMetalSearch = {
            subMetalName = it
            adPostVM.searchSubMetals(subMetalName.text)
        },
        onEnterSubMetalSearch = {
            subMetalName = it
            adPostVM.searchSubMetals(subMetalName.text)
        },
        onCitySelect = { selectedLocation ->
            selectedCity = selectedLocation
        },
        onAddImageClick = {
            launcher.launch("image/*") // Open the gallery to select images
        },
        onAdPostClick = { metalName, desc, price ->
            if (metalName.isEmpty() || desc.isEmpty() || price.isEmpty() || selectedImages.isEmpty()) {
                Toasty.error(
                    context,
                    context.getString(R.string.empty_error),
                    Toast.LENGTH_SHORT,
                    true
                )
                    .show()
                return@AdScreenView
            }
            if (isNetworkAvailable(context)) {
                for (uri in selectedImages) {
                    val base64String = convertImageFileToBase64(uri, context.contentResolver)
                    base64List.add(base64String)
                }
                adPostVM.createPost(
                    CreatePost(
                        userId = "${adPostVM.userPreferences.getUserPreference()?.id}",
                        metalName = mainMetalName.text,
                        submetal = subMetalName.text,
                        city = selectedCity,
                        name = metalName,
                        description = desc,
                        price = price,
                        photos = base64List,
                        phoneNumber = "${adPostVM.userPreferences.getUserPreference()?.phone}"
                    )
                )
                showRewardedAd(context as Activity, rewardedAd = rateVM.rewardedAd,
                    onAdClick = {
                    })
            } else
                Toasty.error(context, noInternetMessage, Toast.LENGTH_SHORT, true)
                    .show()
        },
        onSelectedImageClick = {
            showDialog = true
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdScreenView(
    selectedImages: List<Uri>,
    locationList: List<LocationData>,
    subMetalSearch: TextFieldValue,
    search: TextFieldValue,
    selectedCity: String,
    suggestedSearchList: List<MetalData>?,
    suggestedSubMetalList: List<SubData>?,
    onBackClick: () -> Unit,
    onSearch: (TextFieldValue) -> Unit,
    onSearchClick: (TextFieldValue) -> Unit,
    onCitySelect: (String) -> Unit,
    onAddImageClick: () -> Unit,
    onSelectedImageClick: (Uri) -> Unit,
    onAdPostClick: (String, String, String) -> Unit,
    onSubMetalSearch: (TextFieldValue) -> Unit,
    onEnterSubMetalSearch: (TextFieldValue) -> Unit,
) {
    val currentLocale = Locale.getDefault()
    val isRtl = isRtlLocale(currentLocale)
    var expandedCity by remember { mutableStateOf(false) }
    var description by remember { mutableStateOf("") }
    var metalName by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    val locationData = locationList.map { it.name }
    if (expandedCity) {
        ListDialog(dataList = locationData, onDismiss = { expandedCity = false },
            onConfirm = { locationName ->
                val locationId = locationList.find { it.name == locationName }?.name ?: ""
                onCitySelect(locationId)
                onCitySelect(locationName)
                expandedCity = false
            })
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBG)
            .verticalScroll(rememberScrollState()),
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.statusBarsPadding())
            Header(modifier = Modifier, headerText = stringResource(id = R.string.ad_detail),
                backClick = {
                    onBackClick()
                })
            Spacer(modifier = Modifier.height(10.dp))
            //add image view
            ImagePickerUI(selectedImages,
                onAddImageClick = { onAddImageClick() },
                onSelectedImageClick = { image -> onSelectedImageClick(image) })
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(id = R.string.main_metal),
                color = DarkBlue,
                fontFamily = regularFont,
                textAlign = if (isRtl) TextAlign.End else TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp)
            )
            SearchBar(suggestedSearchList, search,
                onSearch = {
                    onSearch(it)
                },
                onSearchClick = { value ->
                    onSearchClick(value)
                })

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(id = R.string.sub_metal),
                color = DarkBlue,
                fontFamily = regularFont,
                textAlign = if (isRtl) TextAlign.End else TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp)
            )
            SubMetalBar(suggestedSubMetalList, subMetalSearch,
                onSearch = {
                    onSubMetalSearch(it)
                },
                onSearchClick = { value ->
                    onEnterSubMetalSearch(value)
                })
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(id = R.string.name),
                color = DarkBlue,
                fontFamily = regularFont,
                textAlign = if (isRtl) TextAlign.End else TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            CustomTextField(
                modifier = Modifier
                    .height(52.dp)
                    .padding(horizontal = 0.dp),
                value = metalName,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                placeholder = stringResource(id = R.string.metal_name),
                onValueChange = { metalName = it },
                imageId = R.drawable.home_ic
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(id = R.string.price),
                color = DarkBlue,
                fontFamily = regularFont,
                textAlign = if (isRtl) TextAlign.End else TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            CustomTextField(
                modifier = Modifier
                    .height(52.dp)
                    .padding(horizontal = 0.dp),
                value = price,
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
                placeholder = stringResource(id = R.string.price),
                onValueChange = { price = it },
                imageId = R.drawable.home_ic
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(id = R.string.city),
                color = DarkBlue,
                fontFamily = regularFont,
                textAlign = if (isRtl) TextAlign.End else TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp)
                    .clickable {
                        if (locationList.isNotEmpty())
                            expandedCity = true
                    }
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .height(50.dp)
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isRtl) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_location_pin_24),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp),
                        colorFilter = ColorFilter.tint(DarkBlue)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
                Text(
                    text = selectedCity,
                    color = DarkBlue,
                    fontSize = 14.sp,
                    letterSpacing = 2.sp,
                    fontFamily = regularFont,
                    textAlign = if (isRtl) TextAlign.End else TextAlign.Start,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                if (!isRtl) {
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = R.drawable.baseline_location_pin_24),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp),
                        colorFilter = ColorFilter.tint(DarkBlue)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(id = R.string.description),
                color = DarkBlue,
                fontFamily = regularFont,
                textAlign = if (isRtl) TextAlign.End else TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            CustomTextField(
                modifier = Modifier
                    .padding(horizontal = 0.dp)
                    .height(300.dp),
                value = description,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
                placeholder = stringResource(id = R.string.description),
                onValueChange = { description = it },
                imageId = R.drawable.edit_square_ic
            )
            Spacer(modifier = Modifier.height(20.dp))
            AppButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp),
                text = stringResource(id = R.string.ad_post),
                onButtonClick = { onAdPostClick(metalName, description, price) })
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun ImagePickerUI(
    imageList: List<Uri>,
    onAddImageClick: () -> Unit,
    onSelectedImageClick: (Uri) -> Unit,
) {
    val pagerState = rememberPagerState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp)) // Dark background
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                count = imageList.size,
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val uri = imageList[page]
                Box(
                    modifier = Modifier
                        .height(200.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Selected Image $page",
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { onSelectedImageClick(uri) },
                        contentScale = ContentScale.Crop,
                    )
                }
            }
            // Instruction text
            Text(
                text = stringResource(id = R.string.file_size),
                color = Color.Black,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Button
            Button(
                onClick = { onAddImageClick() },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlue), // Button background
            ) {
                Text(
                    text = stringResource(id = R.string.add_images),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = regularFont,
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SubMetalBar(
    suggestedSearchList: List<SubData>?,
    search: TextFieldValue,
    onSearchClick: (TextFieldValue) -> Unit,
    onSearch: (TextFieldValue) -> Unit,
) {
    val currentLocale = Locale.getDefault()
    val isRtl = isRtlLocale(currentLocale)
    var isFocused by remember { mutableStateOf(false) }
    var expandedDropDown by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Observe the search text and update dropdown state based on focus and list availability
    LaunchedEffect(isFocused) {
        expandedDropDown = isFocused && (suggestedSearchList?.isNotEmpty() == true)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExposedDropdownMenuBox(
            expanded = expandedDropDown,
            onExpandedChange = {
                expandedDropDown = !expandedDropDown
            }) {
            OutlinedTextField(
                value = search,
                onValueChange = {
                    onSearch(it)
                    if (it.text.length < search.text.length && !expandedDropDown) {
                        // Backspace detected
                        expandedDropDown = true
                    }
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search),
                        fontFamily = regularFont,
                        letterSpacing = 2.sp,
                        fontSize = 14.sp,
                        maxLines = 1,
                        color = DarkBlue,
                        modifier = Modifier.fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis,
                        textAlign = if (isRtl) TextAlign.End else TextAlign.Start,
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DarkBlue, // Change focused border color
                    unfocusedBorderColor = Color.White, // Change unfocused border color
                    focusedTextColor = DarkBlue,
                    unfocusedTextColor = DarkBlue
                ),
                singleLine = true,
                leadingIcon = if (!isRtl) {
                    {
                        Icon(
                            painterResource(id = R.drawable.search_ic),
                            contentDescription = "",
                            modifier = Modifier.size(24.dp),
                            tint = DarkBlue,
                        )
                    }
                } else null,
                trailingIcon = if (isRtl) {
                    {
                        Icon(
                            painterResource(id = R.drawable.search_ic),
                            contentDescription = "",
                            modifier = Modifier.size(24.dp),
                            tint = DarkBlue,
                        )
                    }
                } else null,
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = regularFont,
                    textAlign = if (isRtl) TextAlign.End else TextAlign.Start, // Align input text as well
                    color = DarkBlue,
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(10.dp))
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    }
                    .menuAnchor(type = MenuAnchorType.PrimaryEditable),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search // Set the IME action to 'Search'
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        expandedDropDown = false
                        keyboardController?.hide()
                        onSearchClick(search)
                    },
                    onDone = {
                        expandedDropDown = false
                        keyboardController?.hide()
                        onSearchClick(search)
                    },
                    onGo = {
                        expandedDropDown = false
                        keyboardController?.hide()
                        onSearchClick(search)
                    },
                    onNext = {
                        expandedDropDown = false
                        keyboardController?.hide()
                        onSearchClick(search)
                    }
                )
            )

            // Dropdown menu below the search bar
            ExposedDropdownMenu(
                expanded = expandedDropDown,
                onDismissRequest = { expandedDropDown = false },
                modifier = Modifier
                    .wrapContentWidth()
                    .background(DarkBlue)
                    .border(width = 0.dp, shape = RoundedCornerShape(10.dp), color = Color.Gray)
            ) {
                suggestedSearchList?.forEachIndexed { index, subMetal ->
                    DropdownMenuItem(
                        text = {
                            Row {
                                Text(
                                    subMetal.name,
                                    color = Color.White,
                                    textAlign = TextAlign.Start
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    subMetal.urduName,
                                    color = Color.White,
                                    textAlign = TextAlign.End
                                )
                            }
                        },
                        onClick = {
                            onSearchClick(
                                TextFieldValue(
                                    text = subMetal.name,
                                    selection = TextRange(subMetal.name.length)
                                )
                            )
                            expandedDropDown = false // Hide dropdown after selection
                        },
                    )
// Add a Divider after each item except the last one
                    if (index < suggestedSearchList.size - 1) {
                        HorizontalDivider(
                            color = Color.White,
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 0.dp)
                        )
                    }
                }
            }
        }
    }
}

// Function to get file size from a Uri
fun getFileSizeFromUri(context: Context, uri: Uri): Long {
    return context.contentResolver.openFileDescriptor(uri, "r")?.use { descriptor ->
        descriptor.statSize
    } ?: 0L
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun AdScreenPreview() {
    PSP_AndroidTheme {
        AdScreenView(
            selectedImages = listOf(),
            listOf(LocationData.mockup), search = TextFieldValue(""), onBackClick = {},
            suggestedSearchList = listOf(),
            suggestedSubMetalList = listOf(),
            onSearch = {},
            onSearchClick = {},
            onCitySelect = {},
            selectedCity = "",
            onAddImageClick = {},
            onAdPostClick = { _, _, _ -> },
            onEnterSubMetalSearch = {},
            onSubMetalSearch = {},
            subMetalSearch = TextFieldValue(""),
            onSelectedImageClick = {}
        )
    }
}