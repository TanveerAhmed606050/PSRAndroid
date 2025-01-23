package com.example.psrandroid.ui.screen.adPost

import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.google.gson.Gson
import es.dmoral.toasty.Toasty

@Composable
fun AdPostScreen(navController: NavController, adPostVM: AdPostVM) {
    val context = LocalContext.current
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
        })

}

@Composable
fun AdPostScreen(
    adsData: List<AdData>?,
    cityList: List<String>?,
    onPlusIconClick: () -> Unit,
    onCityItemClick: (String) -> Unit,
    onAdsClick: (AdData) -> Unit,
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
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Box(
//                    modifier = Modifier
//                        .weight(1f)
//                        .fillMaxWidth(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = stringResource(id = R.string.ad_post),
//                        fontSize = 16.sp,
//                        fontFamily = mediumFont,
//                        color = DarkBlue
//                    )
//                }
//                Box(modifier = Modifier) {
//                    Image(
//                        painter = painterResource(id = R.drawable.filter_ic),
//                        contentDescription = "",
//                        modifier = Modifier
//                            .size(24.dp)
//                            .clickable { onFilter() },
//                        colorFilter = ColorFilter.tint(DarkBlue)
//                    )
//                    DropdownMenu(
//                        expanded = expanded,
//                        onDismissRequest = { expanded = false },
//                        modifier = Modifier
//                            .background(Color.White, RoundedCornerShape(8.dp))
//                            .padding(8.dp)
//                            .wrapContentWidth()
//                            .align(Alignment.TopStart), // Position relative to Box
//                    ) {
//                        DropdownMenuItem(text = {
//                            Text(
//                                text = "Lahore",
//                                fontSize = 16.sp, fontFamily = regularFont
//                            )
//                        }, onClick = { expanded = false })
//                        Spacer(modifier = Modifier.height(8.dp))
//                        DropdownMenuItem(text = {
//                            Text(
//                                text = "Karachi",
//                                fontSize = 16.sp, fontFamily = regularFont
//                            )
//                        }, onClick = { expanded = false })
//                    }
//                }
//            }
            // Header section
            HeaderSection(headerTitle = stringResource(id = R.string.ad_post), cityList = cityList,
                onCityItemClick = { onCityItemClick(it) })
            Spacer(modifier = Modifier.height(20.dp))
            if (adsData?.isEmpty() == true)
                NoProductView(msg = "No Ads", Color.White)
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
                .padding(bottom = 110.dp, end = 16.dp)
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

@Preview
@Composable
fun HomeScreenPreview() {
    PSP_AndroidTheme {
        AdPostScreen(adsData = null,
            cityList = listOf(), onPlusIconClick = {},
            onCityItemClick = {},
            onAdsClick = {})
    }
}