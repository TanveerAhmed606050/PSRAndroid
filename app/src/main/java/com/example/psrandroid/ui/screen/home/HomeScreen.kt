package com.example.psrandroid.ui.screen.home

import android.net.Uri
import android.os.Build
import android.widget.Toast
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.example.psrandroid.network.isNetworkAvailable
import com.example.psrandroid.ui.commonViews.LoadingDialog
import com.example.psrandroid.ui.screen.adPost.models.AdData
import com.example.psrandroid.ui.screen.adPost.models.mockup
import com.example.psrandroid.ui.screen.home.model.HomeScrapRate
import com.example.psrandroid.ui.screen.home.model.LmeMetal
import com.example.psrandroid.ui.screen.home.model.Rate
import com.example.psrandroid.ui.screen.home.model.mockup
import com.example.psrandroid.ui.screen.rate.NoProductView
import com.example.psrandroid.ui.theme.AppBG
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.LightBlue
import com.example.psrandroid.ui.theme.LightGreen40
import com.example.psrandroid.ui.theme.LightRed40
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.ui.theme.boldFont
import com.example.psrandroid.ui.theme.mediumFont
import com.example.psrandroid.ui.theme.regularFont
import com.example.psrandroid.utils.Utils.formatDateDisplay
import com.google.gson.Gson
import es.dmoral.toasty.Toasty

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController, homeVM: HomeVM) {
    val context = LocalContext.current
    var showProgress by remember { mutableStateOf(false) }
    showProgress = homeVM.isLoading
    if (showProgress)
        LoadingDialog()

    var selectedCity by remember {
        mutableStateOf(
            homeVM.userPreferences.getUserPreference()?.location ?: "Lahore"
        )
    }
    val noInternetMessage = stringResource(id = R.string.network_error)

    val cityList =
        homeVM.userPreferences.getLocationList()?.data?.map { "${it.name}" }
    var locationId = homeVM.userPreferences.getLocationList()?.data?.find {
        it.name.equals(homeVM.userPreferences.getUserPreference()?.location, ignoreCase = true)
    }?.id ?: 0 // get location id
    val homeResponse = homeVM.homeResponse
    if (isNetworkAvailable(context)) {
        LaunchedEffect(Unit) {
            homeVM.getHomeData()
        }
    } else
        Toasty.error(context, noInternetMessage, Toast.LENGTH_SHORT, true)
            .show()

    HomeScreenViews(
        selectedCity = selectedCity,
        adsData = homeResponse?.data?.posts,
        homeScrapRate = homeResponse?.data?.homeScrapRates,
        lmeData = homeResponse?.data?.lmeMetals,
        cityList = cityList,
        onCityItemClick = { cityName ->
            selectedCity = cityName
            locationId = homeVM.userPreferences.getLocationList()?.data?.find {
                it.name.equals(selectedCity, ignoreCase = true)
            }?.id ?: 0
        },
        onSeeAllAds = { navController.navigate(Screen.AllPostScreen.route) },
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
    adsData: List<AdData>?,
    homeScrapRate: List<HomeScrapRate>?,
    lmeData: List<LmeMetal>?,
    cityList: List<String>?,
    onCityItemClick: (String) -> Unit,
    onSeeAllAds: () -> Unit,
    onSeeAllRates: () -> Unit,
    onSeeAllLME: () -> Unit,
    onAdsClick: (AdData) -> Unit,
) {
    val selectedScrapRate = homeScrapRate?.find { it.locationName == selectedCity }
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
                    text = stringResource(id = R.string.app_name),
                    fontSize = 16.sp,
                    fontFamily = mediumFont,
                    color = DarkBlue,
                    modifier = Modifier.weight(1f), // Takes equal space and helps centering
                    textAlign = TextAlign.Center,
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                SeeAllView(
                    stringResource(id = R.string.buy_sell),
                    clickAllViews = { onSeeAllAds() })
                Spacer(modifier = Modifier.height(0.dp))
                if (adsData?.isEmpty() == true)
                    NoProductView(msg = stringResource(id = R.string.no_ads), color = DarkBlue)
                else {
                    LazyRow(
                        contentPadding = PaddingValues(vertical = 8.dp),
                        modifier = Modifier.height(220.dp) // Explicit height for LazyRow
                    ) {
                        items(adsData?.size ?: 0) { index ->
                            HomeAdsItems(
                                adsData?.get(index) ?: AdData.mockup, modifier = Modifier
                                    .width(150.dp)
                                    .height(200.dp),
                                onAdsClick = { onAdsClick(it) }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                        }
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
                if (selectedScrapRate?.rates?.isEmpty() == true)
                    NoProductView(msg = stringResource(id = R.string.no_rate), color = DarkBlue)
                else {
                    Spacer(modifier = Modifier.height(0.dp))
                    Card(
                        elevation = CardDefaults.elevatedCardElevation(8.dp), // Use CardDefaults for elevation
                        colors = CardDefaults.cardColors(containerColor = Color.White), // Set the background color
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .heightIn(max = 170.dp)
                            .fillMaxWidth()
                    ) {
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 8.dp),
                        ) {
                            items(selectedScrapRate?.rates?.size ?: 0) { index ->
                                ScrapRateItem(
                                    rateData = selectedScrapRate?.rates?.get(index) ?: Rate(
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                    )
                                )
                                if (index != (selectedScrapRate?.rates?.size?.minus(1) ?: 0))
                                    HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                SeeAllView(stringResource(id = R.string.lme), clickAllViews = { onSeeAllLME() })
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(
                    modifier = Modifier.height(100.dp) // Explicit height for LazyRow
                ) {
                    items(lmeData?.take(3)?.size ?: 0) { index ->
                        HomeLmeItem(lmeData = lmeData?.get(index) ?: LmeMetal.mockup)
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                }
                Spacer(modifier = Modifier.height(120.dp))
            }
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
            .background(
                if (isSelected) DarkBlue else Color.White,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onCityItemClick(cityName) }
    ) {
        Text(
            text = cityName,
            color = if (isSelected) Color.White else LightBlue,
            fontFamily = regularFont,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
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
                model = adData.photos[0], contentDescription = "",
                error = painterResource(id = R.drawable.demo_scrap),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart) // Align the Row at the bottom of the Box
                    .background(Color.White.copy(1f)), // Add some padding for better spacing
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
                        fontFamily = boldFont,
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
fun ScrapRateItem(rateData: Rate) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        Row(
            modifier = Modifier.padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier,
                text = rateData.metalName,
                color = Color.DarkGray, fontSize = 14.sp, fontFamily = regularFont,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis // This will show "..." for truncated text
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                text = "Rs. ${rateData.price}", color = Color.DarkGray, fontSize = 12.sp,
                fontFamily = boldFont, textAlign = TextAlign.End,
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeLmeItem(lmeData: LmeMetal) {
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
            Column {
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
                            if (lmeData.changeInRate.toFloat() < 0) LightRed40 else
                                LightGreen40,
                            RoundedCornerShape(10.dp)
                        )
                        .padding(vertical = 3.dp, horizontal = 6.dp),
                    color = Color.White,
                    fontFamily = mediumFont,
                    fontSize = 12.sp
                )
            }
            Column {
                Text(
                    text = "Rs. 1299",
                    modifier = Modifier
                        .align(Alignment.End),
                    fontSize = 14.sp,
                    fontFamily = boldFont,
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
            fontFamily = mediumFont, fontSize = 14.sp,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(id = R.string.see_all), color = DarkBlue,
            fontFamily = boldFont, fontSize = 12.sp,
            textAlign = TextAlign.End,
            modifier = Modifier
                .padding(3.dp)
                .clickable { clickAllViews() }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun HomeScreenPreview() {
    PSP_AndroidTheme {
        HomeScreenViews(
            selectedCity = "", cityList = listOf(),
            onCityItemClick = {}, adsData = listOf(AdData.mockup),
            lmeData = listOf(LmeMetal.mockup), homeScrapRate = listOf(),
            onSeeAllAds = {},
            onSeeAllRates = {},
            onSeeAllLME = {},
            onAdsClick = {}
        )
    }
}