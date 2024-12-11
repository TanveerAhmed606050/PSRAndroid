package com.example.psrandroid.ui.screen.home

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
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.psp_android.R
import com.example.psrandroid.navigation.Screen
import com.example.psrandroid.network.isNetworkAvailable
import com.example.psrandroid.ui.screen.auth.ListDialog
import com.example.psrandroid.ui.screen.home.models.AdData
import com.example.psrandroid.ui.screen.home.models.mockup
import com.example.psrandroid.ui.screen.rate.NoProductView
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.LightBlue
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.ui.theme.mediumFont
import com.example.psrandroid.utils.isVisible
import com.example.psrandroid.utils.progressBar
import es.dmoral.toasty.Toasty
import io.github.rupinderjeet.kprogresshud.KProgressHUD

@Composable
fun HomeScreen(navController: NavController, homeVM: HomeVM) {
    val context = LocalContext.current
    val adsData = homeVM.adsData
    val progressBar: KProgressHUD = remember { context.progressBar() }
    progressBar.isVisible(homeVM.isLoading)
    val noInternetMessage = stringResource(id = R.string.network_error)
    var expandedCity by remember { mutableStateOf(false) }
    val locationList = homeVM.userPreferences.getLocationList()?.data ?: listOf()
    val locationData = locationList.map { it.name }
//    if (adsData != null) {
//        homeVM.adsData = null
//    }
    if (expandedCity) {
        ListDialog(dataList = locationData, onDismiss = { expandedCity = false },
            onConfirm = { locationName ->
                homeVM.getAdsByLocation(locationName)
                expandedCity = false
            })
    }
    LaunchedEffect(Unit) {
        if (isNetworkAvailable(context))
            homeVM.getAllAds()
        else
            Toasty.error(context, noInternetMessage, Toast.LENGTH_SHORT, true)
                .show()
    }
    HomeScreenView(adsData?.data, onAdClick = {
        navController.navigate(Screen.AdScreen.route)
    },
        onFilter = {
            expandedCity = true
        })
}

@Composable
fun HomeScreenView(
    adsData: List<AdData>?,
    onAdClick: () -> Unit,
    onFilter: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(LightBlue, DarkBlue))),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.statusBarsPadding())
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.home),
                        fontSize = 16.sp,
                        fontFamily = mediumFont,
                        color = Color.White
                    )
                }
                Box(modifier = Modifier) {
                    Image(
                        painter = painterResource(id = R.drawable.filter_ic),
                        contentDescription = "",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onFilter() },
                        colorFilter = ColorFilter.tint(Color.White)
                    )
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
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            if (adsData?.isEmpty() == true)
                NoProductView(msg = "No Ads", Color.White)
            else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 0.dp),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    items(adsData?.size ?: 0) { index ->
                        AdsItemsView(adsData?.get(index) ?: AdData.mockup)
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
        ExtendedFloatingActionButton(
            modifier = Modifier
                .padding(bottom = 110.dp, end = 16.dp)
                .align(Alignment.BottomEnd),
            onClick = {
                onAdClick()
            },
            containerColor = Color.White,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "",
                    tint = Color.Blue,
                    modifier = Modifier.size(20.dp)
                )
            },
            text = {
                Text(
                    text = "Add",
                    color = Color.Blue,
                    fontSize = 16.sp,
                    fontFamily = mediumFont
                )
            }
        )

    }
}

@Composable
fun AdsItemsView(adData: AdData) {
    // State to hold the current image index
//    if (adData.photos.isNotEmpty()) {
    var currentIndex by remember { mutableStateOf(0) }

    // Timer to switch images every 5 seconds
//        LaunchedEffect(currentIndex) {
//            kotlinx.coroutines.delay(5000) // 5 seconds delay
//            currentIndex = (currentIndex + 1) % adData.photos // Loop through images
//        }
    // Display the current image
    Box(
        modifier = Modifier
            .height(250.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Black),
    ) {
        AsyncImage(
            model = adData.photos, contentDescription = "",
            error = painterResource(id = R.drawable.splash_ic),
            contentScale = ContentScale.Crop,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(0.9f))
                .align(Alignment.BottomStart) // Align the Row at the bottom of the Box
                .padding(8.dp), // Add some padding for better spacing
            verticalAlignment = Alignment.Bottom
        ) {
            Column(
                modifier = Modifier.weight(1f), // Take up equal horizontal space
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "${stringResource(id = R.string.metal_name)}: ${adData.metalName}",
                    fontSize = 16.sp,
                    fontFamily = mediumFont,
                    color = Color.Black,
                )
                Text(
                    text = "${stringResource(id = R.string.sub_metal)}: ${adData.submetal}",
                    fontSize = 16.sp,
                    fontFamily = mediumFont,
                    color = Color.Black,
                )
            }
            Column(
                modifier = Modifier.weight(1f), // Take up equal horizontal space
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${stringResource(id = R.string.city)}: ${adData.city}",
                    fontSize = 16.sp,
                    fontFamily = mediumFont,
                    color = Color.Black,
                )
                Text(
                    text = "${stringResource(id = R.string.price)}: ${adData.price}",
                    fontSize = 16.sp,
                    fontFamily = mediumFont,
                    color = Color.Black,
                )
            }
        }
    }
}

@Composable
fun ImageSlider(images: List<Uri>) {
    // State to hold the current image index
    if (images.isNotEmpty()) {
        var currentIndex by remember { mutableIntStateOf(0) }

        // Timer to switch images every 5 seconds
        LaunchedEffect(currentIndex) {
            kotlinx.coroutines.delay(5000) // 5 seconds delay
            currentIndex = (currentIndex + 1) % images.size // Loop through images
        }

        // Display the current image
        Box(
            modifier = Modifier
                .height(200.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = images[currentIndex]),
                contentDescription = "Image Slider",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    PSP_AndroidTheme {
        HomeScreenView(adsData = null, onAdClick = {},
            onFilter = {})
    }
}