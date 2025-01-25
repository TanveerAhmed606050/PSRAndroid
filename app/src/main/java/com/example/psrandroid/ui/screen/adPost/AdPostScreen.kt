package com.example.psrandroid.ui.screen.adPost

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.psp_android.R
import com.example.psrandroid.navigation.Screen
import com.example.psrandroid.network.isNetworkAvailable
import com.example.psrandroid.ui.commonViews.LoadingDialog
import com.example.psrandroid.ui.screen.adPost.models.AdData
import com.example.psrandroid.ui.screen.adPost.models.mockup
import com.example.psrandroid.ui.screen.rate.HeaderSection
import com.example.psrandroid.ui.screen.rate.NoProductView
import com.example.psrandroid.ui.theme.AppBG
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.ui.theme.mediumFont
import com.example.psrandroid.ui.theme.regularFont
import com.example.psrandroid.utils.Utils.isRtlLocale
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdPostScreen(navController: NavController, adPostVM: AdPostVM) {
    val context = LocalContext.current
    var search by remember { mutableStateOf(TextFieldValue("")) }
    var location by remember {
        mutableStateOf(
            adPostVM.userPreferences.getUserPreference()?.location ?: "Lahore"
        )
    }
    val adsData = adPostVM.allAdsData
    var showProgress by remember { mutableStateOf(false) }
    showProgress = adPostVM.isLoading
    if (showProgress)
        LoadingDialog()
    val noInternetMessage = stringResource(id = R.string.network_error)
    val locationList = adPostVM.userPreferences.getLocationList()?.data ?: listOf()
    val locationData = locationList.map { it.name }
    LaunchedEffect(Unit) {
        if (isNetworkAvailable(context))
            adPostVM.getAllAds()
        else
            Toasty.error(context, noInternetMessage, Toast.LENGTH_SHORT, true)
                .show()
    }
    AdPostScreen(
        search = search,
        adsData = adsData?.data,
        cityList = locationData, onPlusIconClick = {
            navController.navigate(Screen.AdScreen.route)
        },
        onCityItemClick = { locationName ->
            location = locationName
            val locationId = adPostVM.userPreferences.getLocationList()?.data?.find {
                it.name.equals(locationName, ignoreCase = true)
            }?.id ?: 0
        },
        onAdsClick = { adData ->
            val adDataJson = Gson().toJson(adData)
            val encodedJson = Uri.encode(adDataJson)
            navController.navigate(Screen.AdDetailScreen.route + "Details/$encodedJson")
        },
        onSearch = {
            search = it
        })

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdPostScreen(
    search: TextFieldValue,
    adsData: List<AdData>?,
    cityList: List<String>?,
    onPlusIconClick: () -> Unit,
    onCityItemClick: (String) -> Unit,
    onAdsClick: (AdData) -> Unit,
    onSearch: (TextFieldValue) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBG),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header section
            HeaderSection(headerTitle = stringResource(id = R.string.ad_post), cityList = cityList,
                onCityItemClick = { onCityItemClick(it) })
            // Search bar
            SearchBar(search,
                onSearchClick = {
                    onSearch(it)
                })
            Spacer(modifier = Modifier.height(10.dp))
            if (adsData?.isEmpty() == true)
                NoProductView(msg = stringResource(id = R.string.no_ads), DarkBlue)
            else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 0.dp),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    items(adsData?.size ?: 0) { index ->
                        AdsItemsView(adsData?.get(index) ?: AdData.mockup, modifier = Modifier,
                            onAdsClick = { onAdsClick(it) }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        if (index + 1 == adsData?.size)
                            Spacer(modifier = Modifier.height(120.dp))
                    }
                }
            }
        }
        ExtendedFloatingActionButton(
            modifier = Modifier
                .padding(bottom = 130.dp, end = 16.dp)
                .align(Alignment.BottomEnd),
            onClick = {
                onPlusIconClick()
            },
            containerColor = Color.White,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "",
                    tint = DarkBlue,
                    modifier = Modifier.size(20.dp)
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.add),
                    color = DarkBlue,
                    fontSize = 16.sp,
                    fontFamily = mediumFont
                )
            }
        )
    }
}

@Composable
fun AdsItemsView(
    adData: AdData,
    modifier: Modifier,
    onAdsClick: (AdData) -> Unit,
) {
    // Display the current image
    Card(
        modifier = modifier
            .height(150.dp)
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(10.dp))
            .clickable { onAdsClick(adData) },
        elevation = CardDefaults.elevatedCardElevation(8.dp), // Use CardDefaults for elevation
        colors = CardDefaults.cardColors(containerColor = Color.White) // Set the background color
    ) {
        Box(
            modifier = Modifier.fillMaxSize() // Make Box take up the full size of the Card
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color.White), // Add some padding for better spacing
            ) {
                Image(
                    painter = painterResource(id = R.drawable.demo_scrap), contentDescription = "",
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .padding(8.dp)
                )
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        text = adData.metalName,
                        fontSize = 12.sp,
                        fontFamily = regularFont,
                        color = Color.DarkGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis // This will show "..." for truncated text
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        text = "PKR ${adData.price}",
                        fontSize = 12.sp,
                        fontFamily = regularFont,
                        color = Color.DarkGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis // This will show "..." for truncated text
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        text = adData.city,
                        fontSize = 12.sp,
                        fontFamily = regularFont,
                        color = Color.DarkGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis // This will show "..." for truncated text
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = adData.description,
                        fontSize = 12.sp,
                        fontFamily = regularFont,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis // This will show "..." for truncated text
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchBar(
    search: TextFieldValue,
    onSearchClick: (TextFieldValue) -> Unit,
) {
    val currentLocale = Locale.getDefault()
    val isRtl = isRtlLocale(currentLocale)
    var isFocused by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = search,
            onValueChange = { value ->
                // Update the search query with the new value and move cursor to the end
                onSearchClick(value.copy(selection = TextRange(value.text.length)))
            },
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
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DarkBlue,
                unfocusedBorderColor = Color.Transparent,
                focusedLabelColor = Color.Transparent,
                unfocusedLabelColor = Color.Transparent,
                focusedTextColor = DarkBlue,
                unfocusedTextColor = Color.Gray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(10))
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search // Set the IME action to 'Search'
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    onSearchClick(search)
                },
                onDone = {
                    keyboardController?.hide()
                    onSearchClick(search)
                },
                onGo = {
                    keyboardController?.hide()
                    onSearchClick(search)
                },
                onNext = {
                    keyboardController?.hide()
                    onSearchClick(search)
                }
            )
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun HomeScreenPreview() {
    PSP_AndroidTheme {
        AdPostScreen(search = TextFieldValue(""), adsData = null,
            cityList = listOf(), onPlusIconClick = {},
            onCityItemClick = {},
            onAdsClick = {},
            onSearch = {})
    }
}