package com.example.psrandroid.ui.screen.home

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.psp_android.R
import com.example.psrandroid.navigation.Screen
import com.example.psrandroid.network.isNetworkAvailable
import com.example.psrandroid.ui.screen.home.models.AdData
import com.example.psrandroid.ui.screen.home.models.mockup
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.LightBlue
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.ui.theme.mediumFont
import com.example.psrandroid.ui.theme.regularFont
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
    LaunchedEffect(Unit) {
        if (isNetworkAvailable(context))
            homeVM.getAllAds()
        else
            Toasty.error(context, noInternetMessage, Toast.LENGTH_SHORT, true)
                .show()
    }
    HomeScreenView(adsData?.data, onAdClick = {
        navController.navigate(Screen.AdScreen.route)
    })
}

@Composable
fun HomeScreenView(
    adsData: List<AdData>?,
    onAdClick: () -> Unit
) {
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
            Text(
                text = stringResource(id = R.string.home), fontSize = 16.sp,
                fontFamily = mediumFont,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 0.dp),
                modifier = Modifier.fillMaxHeight()
            ) {
                items(adsData?.size ?: 0) { index ->
                    AdsItems(adsData?.get(index) ?: AdData.mockup)
                    HorizontalDivider()
                }
            }

//            val imageList = listOf(
//                R.drawable.character_ic,
//                R.drawable.ic_launcher_background,
//                R.drawable.splash_ic,
//                R.drawable.user_placeholder,
//                R.drawable.ic_launcher_background
//            )
//            ImageSlider(images = imageList)
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
fun AdsItems(adData: AdData) {
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
                .height(200.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = R.drawable.home_ic),
                contentDescription = "Image Slider",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = stringResource(id = R.string.metal_name), fontSize = 16.sp,
                        fontFamily = regularFont,
                    )
                    Text(
                        text = stringResource(id = R.string.sub_metal), fontSize = 16.sp,
                        fontFamily = regularFont,
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = stringResource(id = R.string.metal_name), fontSize = 16.sp,
                        fontFamily = regularFont,
                    )
                    Text(
                        text = stringResource(id = R.string.sub_metal), fontSize = 16.sp,
                        fontFamily = regularFont,
                    )
                }
            }
        }
//    }

}

@Composable
fun ImageSlider(images: List<Uri>) {
    // State to hold the current image index
    if (images.isNotEmpty()) {
        var currentIndex by remember { mutableStateOf(0) }

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
        HomeScreenView(adsData = null, onAdClick = {})
    }
}