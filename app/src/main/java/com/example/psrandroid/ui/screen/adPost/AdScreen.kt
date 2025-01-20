package com.example.psrandroid.ui.screen.adPost

import android.content.Context
import android.net.Uri
import android.os.Build
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.psp_android.R
import com.example.psrandroid.response.LocationData
import com.example.psrandroid.response.MetalData
import com.example.psrandroid.response.mockup
import com.example.psrandroid.ui.commonViews.AppButton
import com.example.psrandroid.ui.commonViews.CustomTextField
import com.example.psrandroid.ui.commonViews.Header
import com.example.psrandroid.ui.screen.auth.ListDialog
import com.example.psrandroid.ui.screen.rate.RateVM
import com.example.psrandroid.ui.screen.rate.SearchBar
import com.example.psrandroid.ui.screen.rate.models.SubData
import com.example.psrandroid.ui.theme.AppBG
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.ui.theme.regularFont
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdScreen(navController: NavController, homeVM: AdPostVM, rateVM: RateVM) {
    val context = LocalContext.current
    val locationList = homeVM.userPreferences.getLocationList()?.data ?: listOf()
    var search by remember { mutableStateOf(TextFieldValue("")) }
    var subMetalSearch by remember { mutableStateOf(TextFieldValue("")) }
    var selectedCity by remember { mutableStateOf("Lahore") }
    val suggestedSearchList = rateVM.suggestMainMetals
    val suggestedSubMetalList = homeVM.suggestSubMetals
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var videoUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(Unit) {
        homeVM.getAllSubMetals()
    }
    // Launcher for selecting multiple images
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.size <= 3) { // Allow up to 3 images
            selectedImages = uris
        } else {
            selectedImages = uris.take(3) // Limit to the first 3 images
        }
    }
    // Gallery picker
    val videoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            videoUri = uri
        }
    )
    AdScreenView(
        context,
        videoUri,
        selectedImages,
        locationList = locationList,
        subMetalSearch = subMetalSearch,
        search = search,
        selectedCity = selectedCity,
        suggestedSearchList = suggestedSearchList,
        suggestedSubMetalList = suggestedSubMetalList,
        onBackClick = { navController.popBackStack() },
        onSearch = {
            search = it
            rateVM.searchMainMetals(search.text)
        },
        onSearchClick = {
            search = it
            rateVM.searchMainMetals(search.text)
        },
        onSubMetalSearch = {
            subMetalSearch = it
            homeVM.searchSubMetals(subMetalSearch.text)
        },
        onEnterSubMetalSearch = {
            subMetalSearch = it
            homeVM.searchSubMetals(subMetalSearch.text)
        },
        onCitySelect = { selectedLocation ->
            selectedCity = selectedLocation
        },
        onAddImageClick = {
            launcher.launch("image/*") // Open the gallery to select images
        },
        onAddVideoClick = {
            videoLauncher.launch("video/*")
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdScreenView(
    context: Context,
    videoUri: Uri?,
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
    onAddVideoClick: () -> Unit,
    onSubMetalSearch: (TextFieldValue) -> Unit,
    onEnterSubMetalSearch: (TextFieldValue) -> Unit,
) {
    var expandedCity by remember { mutableStateOf(false) }
    var description by remember { mutableStateOf("") }
    var metalName by remember { mutableStateOf("") }
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
        }
        //add image view
        ImagePickerUI(selectedImages,
            onAddImageClick = { onAddImageClick() })
        Spacer(modifier = Modifier.height(10.dp))
//        VideoViewUI(context = context, videoUri = videoUri,
//            onAddVideoClick = { onAddVideoClick() })
//        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(id = R.string.main_metal),
            color = DarkBlue,
            fontFamily = regularFont,
            modifier = Modifier.padding(start = 20.dp)
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
            modifier = Modifier.padding(start = 20.dp)
        )
        SearchBar(suggestedSearchList, subMetalSearch,
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
            modifier = Modifier.padding(start = 20.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        CustomTextField(
            modifier = Modifier
                .height(52.dp)
                .padding(horizontal = 20.dp),
            value = metalName,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            placeholder = stringResource(id = R.string.metal_name),
            onValueChange = { metalName = it },
            imageId = R.drawable.home_ic
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(id = R.string.city),
            color = DarkBlue,
            fontFamily = regularFont,
            modifier = Modifier.padding(start = 20.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clickable {
                    if (locationList.isNotEmpty())
                        expandedCity = true
                }
                .background(Color.White, RoundedCornerShape(12.dp))
                .height(50.dp)
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(12.dp),
                    color = colorResource(id = R.color.text_grey)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedCity,
                color = DarkBlue,
                fontSize = 14.sp,
                letterSpacing = 2.sp,
                fontFamily = regularFont,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.baseline_location_pin_24),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp),
                colorFilter = ColorFilter.tint(DarkBlue)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(id = R.string.description),
            color = Color.White,
            fontFamily = regularFont,
            modifier = Modifier.padding(start = 20.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        CustomTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp)
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
                .padding(bottom = 30.dp, start = 20.dp, end = 20.dp),
            text = stringResource(id = R.string.ad_post),
            onButtonClick = {})
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun ImagePickerUI(
    imageList: List<Uri>,
    onAddImageClick: () -> Unit
) {
    val pagerState = rememberPagerState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
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
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
            // Instruction text
            Text(
                text = "5MB maximum file size accepted in the\nfollowing formats: jpg, jpeg, png, .gif",
                color = Color.Black,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Button
            Button(
                onClick = { onAddImageClick() },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlue) // Button background
            ) {
                Text(
                    text = "Add images",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun AdScreenPreview() {
    PSP_AndroidTheme {
        AdScreenView(
            context = LocalContext.current,
            videoUri = null,
            selectedImages = listOf(),
            listOf(LocationData.mockup), search = TextFieldValue(""), onBackClick = {},
            suggestedSearchList = listOf(),
            suggestedSubMetalList = listOf(),
            onSearch = {},
            onSearchClick = {},
            onCitySelect = {},
            selectedCity = "",
            onAddImageClick = {},
            onAddVideoClick = {},
            onEnterSubMetalSearch = {},
            onSubMetalSearch = {},
            subMetalSearch = TextFieldValue(""),
        )
    }
}