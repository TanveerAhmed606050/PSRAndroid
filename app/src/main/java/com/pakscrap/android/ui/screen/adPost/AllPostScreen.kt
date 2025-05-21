package com.pakscrap.android.ui.screen.adPost

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.pakscrap.android.ui.screen.adPost.models.AdPostDto
import com.pakscrap.android.navigation.Screen
import com.pakscrap.android.network.isNetworkAvailable
import com.google.gson.Gson
import com.pakscrap.R
import com.pakscrap.android.ui.commonViews.Header
import com.pakscrap.android.ui.screen.adPost.models.AdsData
import com.pakscrap.android.ui.screen.adPost.models.mockup
import com.pakscrap.android.ui.screen.home.CityItems
import com.pakscrap.android.ui.screen.rate.NoProductView
import com.pakscrap.android.ui.theme.AppBG
import com.pakscrap.android.ui.theme.DarkBlue
import com.pakscrap.android.ui.theme.PSP_AndroidTheme
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.flow.flowOf

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AllPostScreen(navController: NavController, adPostVM: AdPostVM) {
    val context = LocalContext.current
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var selectedCity by rememberSaveable {
        mutableStateOf(
            adPostVM.userPreferences.getUserData()?.location ?: "Lahore"
        )
    }
    val adsByCities = adPostVM.adsByCities.collectAsLazyPagingItems()
    val connectivityError = stringResource(id = R.string.network_error)
    val citiesList = adPostVM.userPreferences.getCitiesList()?.data ?: listOf()
    val citiesData = citiesList.map { it.name }
    LaunchedEffect(selectedCity, searchText) {
        if (isNetworkAvailable(context))
            adPostVM.getAdsByCities(
                AdPostDto(
                    metalName = searchText.text,
                    city = selectedCity,
                    perPage = "10",
                    page = "1"
                )
            )
        else
            Toasty.error(context, connectivityError, Toast.LENGTH_SHORT, false)
                .show()
    }
    AllPostScreenUI(selectedCity = selectedCity,
        search = searchText,
        adsData = adsByCities,
        cityList = citiesData,
        onCityItemClick = { locationName ->
            selectedCity = locationName
        },
        onAdsClick = { adData ->
            val adDataJson = Gson().toJson(adData)
            val encodedJson = Uri.encode(adDataJson)
            val isMyAd = false
            navController.navigate(Screen.AdDetailScreen.route + "Details/$encodedJson/$isMyAd")
        },
        onSearch = {
            searchText = it
        },
        onBackClick = {
            navController.popBackStack()
        })

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AllPostScreenUI(
    selectedCity: String,
    search: TextFieldValue,
    adsData: LazyPagingItems<AdsData>,
    cityList: List<String>?,
    onCityItemClick: (String) -> Unit,
    onAdsClick: (AdsData) -> Unit,
    onSearch: (TextFieldValue) -> Unit,
    onBackClick: () -> Unit,
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
            Spacer(modifier = Modifier.statusBarsPadding())
            // Header section
            Header(
                modifier = Modifier,
                headerText = stringResource(id = R.string.all_post),
                backClick = { onBackClick() })
            Spacer(modifier = Modifier.height(10.dp))

            if (adsData.loadState.refresh is LoadState.Loading ||
                adsData.loadState.append is LoadState.Loading
            )
                LinearProgressIndicator()
            Spacer(modifier = Modifier.height(10.dp))
            // Search bar
            SearchBar(search,
                onSearchClick = {
                    onSearch(it)
                })
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(
                contentPadding = PaddingValues(vertical = 8.dp),
            ) {
                items(cityList?.size ?: 0) { index ->
                    val cityName = cityList?.get(index) ?: ""
                    CityItems(
                        cityName = cityName,
                        selectedCity = selectedCity,
                        onCityItemClick = { city ->
                            onCityItemClick(city)
                        }
                    )
                }

            }
            Spacer(modifier = Modifier.height(10.dp))
            if (adsData.itemCount == 0)
                NoProductView(msg = stringResource(id = R.string.no_ads), DarkBlue)
            else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 0.dp),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    items(adsData.itemCount) { index ->
                        AdsItemsView(
                            adsData[index] ?: AdsData.mockup,
                            isMyAd = false, modifier = Modifier,
                            onAdsClick = { onAdsClick(it) }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        if (index + 1 == adsData.itemCount)
                            Spacer(modifier = Modifier.height(50.dp))
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun AllPostScreenPreview() {
    PSP_AndroidTheme {
        AllPostScreenUI(selectedCity = "",
            search = TextFieldValue(""),
            adsData = flowOf(PagingData.from(listOf(AdsData.mockup))).collectAsLazyPagingItems(),
            cityList = listOf(),
            onCityItemClick = {},
            onAdsClick = {},
            onSearch = {},
            onBackClick = {})
    }
}