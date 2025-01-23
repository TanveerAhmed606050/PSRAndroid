package com.example.psrandroid.ui.screen.home

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.psp_android.R
import com.example.psrandroid.navigation.Screen
import com.example.psrandroid.response.AuthData
import com.example.psrandroid.response.LmeData
import com.example.psrandroid.response.SubMetalData
import com.example.psrandroid.response.mockup
import com.example.psrandroid.ui.commonViews.MyAsyncImage
import com.example.psrandroid.ui.screen.adPost.models.AdData
import com.example.psrandroid.ui.screen.adPost.models.mockup
import com.example.psrandroid.ui.theme.AppBG
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.LightBlue
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.ui.theme.mediumFont
import com.example.psrandroid.ui.theme.regularFont
import com.example.psrandroid.utils.Utils.formatDateDisplay
import com.google.gson.Gson

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController, homeVM: HomeVM) {
    var selectedCity by remember {
        mutableStateOf(
            homeVM.userPreferences.getUserPreference()?.location ?: "Lahore"
        )
    }
    val cityList =
        homeVM.userPreferences.getLocationList()?.data?.map { "${it.name}" }
    HomeScreenViews(selectedCity = selectedCity,
        userData = homeVM.userPreferences.getUserPreference() ?: AuthData.mockup,
        adsData = null,
        onProfileImageClick = {
            navController.navigate(Screen.MyProfileScreen.route)
        },
        cityList = cityList,
        onCityItemClick = { cityName ->
            selectedCity = cityName
        },
        onSeeAllAds = { navController.navigate(Screen.AdPostScreen.route) },
        onSeeAllRates = { navController.navigate(Screen.RateScreen.route) },
        onSeeAllLME = { navController.navigate(Screen.LmeScreen.route) },
        onAdsClick = { adData ->
            val adDataJson = Gson().toJson(adData)
            val encodedJson = Uri.encode(adDataJson)
            navController.navigate(Screen.AdDetailScreen.route + "Details/$encodedJson")
        })
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreenViews(
    selectedCity: String,
    userData: AuthData,
    adsData: List<AdData>?,
    cityList: List<String>?,
    onProfileImageClick: () -> Unit,
    onCityItemClick: (String) -> Unit,
    onSeeAllAds: () -> Unit,
    onSeeAllRates: () -> Unit,
    onSeeAllLME: () -> Unit,
    onAdsClick: (AdData) -> Unit,
) {
    //UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBG)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.statusBarsPadding())
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.SpaceBetween, // Ensures content is spaced properly
                verticalAlignment = Alignment.CenterVertically // Aligns content vertically centered
            ) {
                Text(
                    text = stringResource(id = R.string.home),
                    fontSize = 16.sp,
                    fontFamily = mediumFont,
                    color = DarkBlue,
                    modifier = Modifier.weight(1f), // Takes equal space and helps centering
                    textAlign = TextAlign.Center,
                )
                Row(
                    modifier = Modifier
                        .padding(start = 0.dp)
                        .clickable { onProfileImageClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MyAsyncImage(imageUrl = userData.profilePic, 40.dp, true)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            SeeAllView(stringResource(id = R.string.buy_sell), clickAllViews = { onSeeAllAds() })
            Spacer(modifier = Modifier.height(0.dp))
            LazyRow(
                contentPadding = PaddingValues(vertical = 8.dp),
                modifier = Modifier.height(220.dp) // Explicit height for LazyRow
            ) {
                items(3) { index ->
                    HomeAdsItems(
                        adsData?.get(index) ?: AdData.mockup, modifier = Modifier
                            .width(150.dp)
                            .height(200.dp),
                        onAdsClick = { onAdsClick(it) }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            SeeAllView(
                stringResource(id = R.string.scrap_rate),
                clickAllViews = { onSeeAllRates() })

            Spacer(modifier = Modifier.height(5.dp))
            LazyRow(
                contentPadding = PaddingValues(vertical = 8.dp),
                modifier = Modifier.height(60.dp) // Explicit height for LazyRow
            ) {
                items(cityList?.size ?: 0) { index ->
                    val cityName = cityList?.get(index) ?: ""
                    CityItems(
                        cityName = cityName,
                        selectedCity = selectedCity, // Check if this city is selected
                        onCityItemClick = { city ->
                            onCityItemClick(city) // Trigger the callback
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(0.dp))
            LazyRow(
                contentPadding = PaddingValues(vertical = 8.dp),
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth() // Explicit height for LazyRow
            ) {
                items(3) { index ->
                    ScrapRateItem(metalDetail = SubMetalData.mockup)
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            SeeAllView(stringResource(id = R.string.lme), clickAllViews = { onSeeAllLME() })
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(
                modifier = Modifier.height(100.dp) // Explicit height for LazyRow
            ) {
                items(3) { index ->
                    HomeLmeItem(LmeData.mockup)
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
fun CityItems(
    cityName: String,
    selectedCity: String, // Add a flag to indicate selection
    onCityItemClick: (String) -> Unit,
) {
    val isSelected = selectedCity.contains(cityName, ignoreCase = true)  // false
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
//            .border(width = 1.dp, color = Color.White, RoundedCornerShape(30.dp))
            .background(
                if (isSelected) DarkBlue else Color.White,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onCityItemClick(cityName) }
    ) {
        Text(
            text = cityName,
            color = if (isSelected) Color.White else LightBlue,
            fontFamily = regularFont,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun HomeAdsItems(
    adData: AdData,
    modifier: Modifier,
    onAdsClick: (AdData) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White, RoundedCornerShape(10.dp))
            .clickable { onAdsClick(adData) },
        elevation = CardDefaults.elevatedCardElevation(8.dp), // Use CardDefaults for elevation
        colors = CardDefaults.cardColors(containerColor = Color.White) // Set the background color
    ) {
        Box(
            modifier = Modifier.fillMaxSize() // Make Box take up the full size of the Card
        ) {
            AsyncImage(
                model = adData.photos, contentDescription = "",
                error = painterResource(id = R.drawable.demo_scrap),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart) // Align the Row at the bottom of the Box
                    .background(Color.White.copy(1f)), // Add some padding for better spacing
//                verticalAlignment = Alignment.Bottom,
//                horizontalArrangement = Arrangement.Start
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 8.dp), // Take up equal horizontal space
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = adData.metalName,
                        fontSize = 14.sp,
                        fontFamily = regularFont,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(0.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "PKR ${adData.price}",
                        fontSize = 14.sp,
                        fontFamily = regularFont,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(0.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = adData.city,
                        fontSize = 14.sp,
                        fontFamily = regularFont,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

@Composable
fun ScrapRateItem(metalDetail: SubMetalData?) {
    Card(
        modifier = Modifier
            .height(180.dp)
            .fillMaxWidth() // Ensures the row takes the full width of the parent
            .background(Color.White, RoundedCornerShape(10.dp))
            .clickable { },
        elevation = CardDefaults.elevatedCardElevation(8.dp), // Use CardDefaults for elevation
        colors = CardDefaults.cardColors(containerColor = Color.White) // Set the background color
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceAround,
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.width(74.dp),
                    text = "Iron",
                    color = Color.DarkGray, fontSize = 14.sp, fontFamily = regularFont,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis // This will show "..." for truncated text
                )
//                Spacer(modifier = Modifier.weight(1f)) // Pushes the second Text to the end
                Text(
                    modifier = Modifier,
                    text = "Rs. 2150", color = Color.DarkGray, fontSize = 12.sp,
                    fontFamily = regularFont, textAlign = TextAlign.Center,
                )
            }
            Row(
                modifier = Modifier
            ) {
                Text(
                    modifier = Modifier.width(74.dp),
                    text = "Copper",
                    color = Color.DarkGray, fontSize = 14.sp, fontFamily = regularFont,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis // This will show "..." for truncated text
                )
                Text(
                    modifier = Modifier, // Occupies 1x space
                    text = "Rs. 102", color = Color.DarkGray, fontSize = 12.sp,
                    fontFamily = regularFont, textAlign = TextAlign.End,
                )
            }
            Row(
                modifier = Modifier
            ) {
                Text(
                    modifier = Modifier.width(74.dp),
                    text = "Silver",
                    color = Color.DarkGray, fontSize = 14.sp, fontFamily = regularFont,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis // This will show "..." for truncated text
                )
                Text(
                    modifier = Modifier.background(color= DarkBlue,
                    RoundedCornerShape(4.dp)).padding(5.dp,0.dp,5.dp,0.dp),
                    text = stringResource(id = R.string.watch_ad),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontFamily = regularFont,
                    textAlign = TextAlign.End
                )
            }
            Row(
                modifier = Modifier
            ) {
                Text(
                    modifier = Modifier.width(74.dp),
                    text = "Plastic",
                    color = Color.DarkGray, fontSize = 14.sp, fontFamily = regularFont,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis // This will show "..." for truncated text
                )
                Text(
                    modifier = Modifier,
                    text = "Rs. ****",
                    color = Color.DarkGray,
                    fontSize = 12.sp,
                    fontFamily = regularFont,
                    textAlign = TextAlign.End,
                )
            }
            Row(
                modifier = Modifier
            ) {
                Text(
                    modifier = Modifier.width(74.dp),
                    text = "Nikal",
                    color = Color.DarkGray, fontSize = 14.sp, fontFamily = regularFont,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis // This will show "..." for truncated text
                )
                Text(
                    modifier = Modifier,
                    text = "Rs. ****",
                    color = Color.DarkGray,
                    fontSize = 12.sp,
                    fontFamily = regularFont,
                    textAlign = TextAlign.End,
                )
            }
//                Row(
//                    modifier = Modifier.weight(3f),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        modifier = Modifier
//                            .padding(bottom = 4.dp)
//                            .border(
//                                color = DarkBlue,
//                                shape = RoundedCornerShape(4.dp),
//                                width = 0.8.dp
//                            )
//                            .padding(6.dp),
//                        text = stringResource(id = R.string.watch_ad),
//                        color = DarkBlue,
//                        fontSize = 14.sp,
//                        fontFamily = regularFont,
//                        textAlign = TextAlign.Center,
//                    )
//                }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeLmeItem(lmeData: LmeData) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White, RoundedCornerShape(10.dp))
            .clickable { },
        elevation = CardDefaults.elevatedCardElevation(8.dp), // Use CardDefaults for elevation
        colors = CardDefaults.cardColors(containerColor = Color.White) // Set the background color
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
//                modifier = Modifier.padding(8.dp),
            ) {
                Text(
                    modifier = Modifier.width(70.dp),
                    text = lmeData.name,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    maxLines = 1,
                    fontFamily = regularFont,
                    overflow = TextOverflow.Ellipsis // This will show "..." for truncated text
                )
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
                Text(
                    text = "${lmeData.changeInRate}%", modifier = Modifier
                        .background(
                            if (lmeData.changeInRate.toFloat() < 0) Color.Red else
                                Color.Green,
                            RoundedCornerShape(10.dp)
                        )
                        .padding(vertical = 3.dp, horizontal = 6.dp),
                    color = Color.White,
                    fontFamily = mediumFont,
                    fontSize = 12.sp
                )
            }
            Column(
//                modifier = Modifier.padding(4.dp),
            ) {
                Text(
                    text = "Rs. ${lmeData.price}",
                    modifier = Modifier
                        .align(Alignment.End),
                    fontSize = 14.sp,
                    fontFamily = regularFont,
                    color = Color.DarkGray,
                )
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
                Text(
                    text = "Expire ${formatDateDisplay(lmeData.expiryDate)}",
                    modifier = Modifier,
                    fontSize = 10.sp,
                    fontFamily = regularFont,
                    color = Color.DarkGray,
                    letterSpacing = 1.sp,
                )
            }
        }
    }
}

@Composable
fun SeeAllView(text: String, clickAllViews: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = text, color = Color.DarkGray,
            fontFamily = regularFont, fontSize = 16.sp,
        )
        Text(
            text = stringResource(id = R.string.see_all), color = DarkBlue,
            fontFamily = mediumFont, fontSize = 12.sp,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth().padding(3.dp)
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
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MyAsyncImage(imageUrl = userData.profilePic, 50.dp, true)
//        Column(
//            modifier = Modifier
//                .weight(1f)
//                .padding(start = 8.dp),
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//            ) {
//                Text(
//                    text = userData.name,
//                    fontFamily = mediumFont,
//                    fontSize = 16.sp,
//                    color = Color.Black
//                )
//                Spacer(modifier = Modifier.padding(start = 4.dp))
//                Image(
//                    painter = painterResource(id = R.drawable.next_blk),
//                    contentDescription = "",
//                    modifier = Modifier
//                        .padding(start = 4.dp)
//                        .size(20.dp), // Set desired size
//                    colorFilter = ColorFilter.tint(Color.Black)
//                )
//            }
//            Text(
//                text = userData.phone,
//                fontFamily = regularFont,
//                color = Color.Gray,
//                fontSize = 16.sp,
//            )
//        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun HomeScreenPreview() {
    PSP_AndroidTheme {
        HomeScreenViews(selectedCity = "",
            AuthData.mockup, onProfileImageClick = {}, cityList = listOf(),
            onCityItemClick = {}, adsData = null,
            onSeeAllAds = {},
            onSeeAllRates = {},
            onSeeAllLME = {},
            onAdsClick = {}
        )
    }
}