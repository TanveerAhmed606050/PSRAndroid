package com.example.psrandroid.ui.screen.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.psp_android.R
import com.example.psrandroid.navigation.Screen
import com.example.psrandroid.response.AuthData
import com.example.psrandroid.response.LmeData
import com.example.psrandroid.response.SubMetalData
import com.example.psrandroid.response.mockup
import com.example.psrandroid.ui.commonViews.MyAsyncImage
import com.example.psrandroid.ui.screen.adPost.AdsItemsView
import com.example.psrandroid.ui.screen.adPost.models.AdData
import com.example.psrandroid.ui.screen.adPost.models.mockup
import com.example.psrandroid.ui.screen.lme.LmeItem
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.ui.theme.mediumFont
import com.example.psrandroid.ui.theme.regularFont

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController, homeVM: HomeVM) {
    val cityList =
        homeVM.userPreferences.getLocationList()?.data?.map { "${it.name} ${it.urduName}" }
    HomeScreenViews(userData = AuthData.mockup, adsData = null,
        onProfileImageClick = {
            navController.navigate(Screen.MyProfileScreen.route)
        }, cityList = cityList,
        onCityItemClick = { cityName ->
        })
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreenViews(
    userData: AuthData,
    adsData: List<AdData>?,
    cityList: List<String>?,
    onProfileImageClick: () -> Unit,
    onCityItemClick: (String) -> Unit,
) {
    var selectedCity by remember { mutableStateOf<String?>(null) }
    //UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.statusBarsPadding())
            Text(
                text = stringResource(id = R.string.home),
                fontSize = 16.sp,
                fontFamily = mediumFont,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            ProfileImageView(userData) {
                onProfileImageClick()
            }
            Spacer(modifier = Modifier.height(20.dp))
            LazyRow(
                contentPadding = PaddingValues(vertical = 8.dp),
            ) {
                items(4) {
                    CityItems(cityName = "Lahore لاہور", isSelected = false) {
                    }
                }
//                items(cityList?.size?:0) { index ->
//                    val cityName = cityList?.get(index) ?:""
//                    CityItems(
//                        cityName = cityName,
//                        isSelected = cityName == selectedCity, // Check if this city is selected
//                        onCityItemClick = { city ->
//                            selectedCity = city // Update the selected city
//                            onCityItemClick(city) // Trigger the callback
//                        }
//                    )
//                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            SeeAllView(stringResource(id = R.string.buy_sell), clickAllViews = {})
            LazyRow(
                contentPadding = PaddingValues(vertical = 8.dp),
            ) {
                items(3) { index ->
                    AdsItemsView(
                        adsData?.get(index) ?: AdData.mockup, modifier = Modifier
                            .width(250.dp)
                            .height(300.dp)
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            SeeAllView(stringResource(id = R.string.scrap_rate), clickAllViews = {})
            LazyRow(
                contentPadding = PaddingValues(vertical = 8.dp),
            ) {
                items(3) { index ->
                    ScrapRateItem(metalDetail = SubMetalData.mockup)
                    Spacer(modifier = Modifier.width(20.dp))
                }
            }
            SeeAllView(stringResource(id = R.string.lme), clickAllViews = {})
            LazyRow(
                contentPadding = PaddingValues(vertical = 8.dp),
            ) {
                items(3) { index ->
                    LmeItem(LmeData.mockup)
                    Spacer(modifier = Modifier.width(20.dp))
                }
            }
        }
    }
}

@Composable
fun CityItems(
    cityName: String,
    isSelected: Boolean, // Add a flag to indicate selection
    onCityItemClick: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .background(
                if (isSelected) Color.LightGray else Color.White,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onCityItemClick(cityName) }
    ) {
        Text(
            text = cityName, color = DarkBlue, fontFamily = mediumFont,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun ScrapRateItem(metalDetail: SubMetalData?) {
    Column(
        modifier = Modifier
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${metalDetail?.submetalName} ${metalDetail?.submetalUrduName}",
            color = DarkBlue, fontSize = 16.sp, fontFamily = regularFont,
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "${metalDetail?.price}", color = DarkBlue, fontSize = 18.sp,
            fontFamily = mediumFont, textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun SeeAllView(text: String, clickAllViews: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = text, color = Color.White,
            fontFamily = mediumFont, fontSize = 18.sp
        )
        Text(
            text = stringResource(id = R.string.see_all), color = Color.White,
            fontFamily = mediumFont, fontSize = 18.sp,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { clickAllViews() }
        )
    }
}

@Composable
fun ProfileImageView(userData: AuthData, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 0.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MyAsyncImage(imageUrl = userData.profilePic, 50.dp, true)
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = userData.name,
                    fontFamily = mediumFont,
                    fontSize = 18.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.padding(start = 4.dp))
                Image(
                    painter = painterResource(id = R.drawable.next_blk),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(20.dp), // Set desired size
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
            Text(
                text = userData.phone,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                fontSize = 16.sp,
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun HomeScreenPreview() {
    PSP_AndroidTheme {
        HomeScreenViews(
            AuthData.mockup, onProfileImageClick = {}, cityList = listOf(),
            onCityItemClick = {}, adsData = null
        )
    }
}