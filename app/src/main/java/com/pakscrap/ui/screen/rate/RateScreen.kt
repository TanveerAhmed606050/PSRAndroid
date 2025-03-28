package com.pakscrap.ui.screen.rate

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.pakscrap.R
import com.pakscrap.network.isNetworkAvailable
import com.pakscrap.ui.commonViews.GoogleInterstitialAd
import com.pakscrap.ui.commonViews.LoadingDialog
import com.pakscrap.ui.commonViews.showInterstitialAd
import com.pakscrap.ui.screen.home.CityItems
import com.pakscrap.ui.screen.rate.models.MainMetalData
import com.pakscrap.ui.screen.rate.models.MetalData
import com.pakscrap.ui.screen.rate.models.SubMetals
import com.pakscrap.ui.theme.AppBG
import com.pakscrap.ui.theme.DarkBlue
import com.pakscrap.ui.theme.LightBlue
import com.pakscrap.ui.theme.PSP_AndroidTheme
import com.pakscrap.ui.theme.mediumFont
import com.pakscrap.ui.theme.regularFont
import com.pakscrap.utils.Utils.isRtlLocale
import es.dmoral.toasty.Toasty
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RateScreen(rateVM: RateVM) {
    val context = LocalContext.current
    var isRefreshing by remember { mutableStateOf(false) }
    var showProgress by remember { mutableStateOf(false) }
    showProgress = rateVM.isLoading
    if (showProgress)
        LoadingDialog()
    if (!rateVM.isLoading)
        isRefreshing = false
    //show Interstitial Ad
    GoogleInterstitialAd(context, onAdClick = {})
    if (!rateVM.watchInterstitialAd) {
        showInterstitialAd(context)
        rateVM.watchInterstitialAd = true
    }

    var locationId = rateVM.userPreferences.getLocationList()?.data?.find {
        it.name.equals(rateVM.userPreferences.getUserPreference()?.location, ignoreCase = true)
    }?.id ?: 0 // get location id

    val locationList = rateVM.userPreferences.getLocationList()?.data ?: listOf()

    var search by remember { mutableStateOf(TextFieldValue("")) }
    val sharedPreferences = rateVM.userPreferences

    var location by remember { mutableStateOf("Lahore") }
    val noInternetMessage = stringResource(id = R.string.network_error)
    val locationData = locationList.map { it.name }

    val subMetalData = rateVM.subMetalResponse
    val suggestedSearchList = rateVM.suggestMainMetals

    if (isNetworkAvailable(context)) {
        LaunchedEffect(key1 = Unit) {
            location = sharedPreferences.getUserPreference()?.location ?: "Lahore"
            rateVM.getMainMetals("$locationId", "")
            rateVM.getSubMetals("$locationId", search.text)
        }
    } else
        Toasty.error(context, noInternetMessage, Toast.LENGTH_SHORT, false)
            .show()

    DashBoardScreen(search, location,
        cityList = locationData,
        productList = subMetalData?.data, isRefreshing,
        suggestedSearchList = suggestedSearchList,
        onSearch = { query ->
            search = query
            rateVM.searchMainMetals(search.text)
        },
        onPullDown = {
            isRefreshing = true
            if (isNetworkAvailable(context)) {
                if (subMetalData == null)
                    rateVM.getSubMetals("$locationId", search.text)
            } else
                Toasty.error(
                    context,
                    noInternetMessage,
                    Toast.LENGTH_SHORT,
                    false
                )
                    .show()

        },
        onSearchClick = { searchText ->
            search = searchText
            rateVM.userPreferences.lastSearchMetal = searchText.text
            locationId = rateVM.userPreferences.getLocationList()?.data?.find {
                it.name.equals(location, ignoreCase = true)
            }?.id ?: 0
            if (isNetworkAvailable(context)) {
                rateVM.getSubMetals("$locationId", searchText.text)
            } else
                Toasty.error(
                    context, noInternetMessage, Toast.LENGTH_SHORT, false
                ).show()
        },
        onCityItemClick = { locationName ->
            location = locationName
            locationId = rateVM.userPreferences.getLocationList()?.data?.find {
                it.name.equals(locationName, ignoreCase = true)
            }?.id ?: 0
            rateVM.getSubMetals("$locationId", search.text)
        })
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashBoardScreen(
    search: TextFieldValue,
    selectedCity: String,
    cityList: List<String>?,
    productList: List<MetalData>?,
    isRefreshing: Boolean,
    suggestedSearchList: List<MainMetalData>?,
    onSearchClick: (TextFieldValue) -> Unit,
    onSearch: (TextFieldValue) -> Unit,
    onPullDown: () -> Unit,
    onCityItemClick: (String) -> Unit,
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = {
            onPullDown()
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBG) // Background color
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(bottom = 120.dp)
            ) {
                Spacer(modifier = Modifier.statusBarsPadding())
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.scrap_rate), fontSize = 16.sp,
                    fontFamily = mediumFont,
                    color = DarkBlue,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                // Search bar
                SearchBar(suggestedSearchList, search,
                    onSearch = {
                        onSearch(it)
                    },
                    onSearchClick = { value ->
                        onSearchClick(value)
                    })
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

                if (productList?.isEmpty() != false)
                    NoProductView(stringResource(id = R.string.no_rate), DarkBlue)
                else {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 0.dp)
                    ) {
                        items(productList.size) { index ->
                            ProductList(productList[index], search)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(120.dp))

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchBar(
    suggestedSearchList: List<MainMetalData>?,
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
                onValueChange = { value ->
                    // Update the search query with the new value and move cursor to the end
                    onSearch(value.copy(selection = TextRange(value.text.length)))
                    if (value.text.length < search.text.length && !expandedDropDown) {
                        // Backspace detected
                        expandedDropDown = true
                    }
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
                    }
                    .menuAnchor(type = MenuAnchorType.PrimaryEditable),
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Sentences,
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
                suggestedSearchList?.forEachIndexed { index, mainMetal ->
                    DropdownMenuItem(
                        text = {
                            Row {
                                Text(
                                    mainMetal.name,
                                    color = Color.White,
                                    textAlign = TextAlign.Start
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    mainMetal.urduName,
                                    color = Color.White,
                                    textAlign = TextAlign.End
                                )
                            }
                        },
                        onClick = {
                            onSearchClick(
                                TextFieldValue(
                                    text = mainMetal.name,
                                    selection = TextRange(mainMetal.urduName.length)
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

@Composable
fun ProductList(mainMetalData: MetalData, search: TextFieldValue) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 4.dp)
            .clickable { }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "${mainMetalData.metalName} Scrap",
                color = LightBlue,
                fontSize = 14.sp,
                fontFamily = mediumFont
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.price_kg),
                color = LightBlue,
                fontFamily = mediumFont,
                fontSize = 14.sp,
                modifier = Modifier.padding(end = 0.dp)
            )
        }
        mainMetalData.submetals.forEach { submetal ->
            ProductItem(submetal)
            HorizontalDivider()
        }
        Log.d("lsdjg", "search: ${search.text}")
        if (search.text.isNotEmpty()) {
            Text(
                text = mainMetalData.metalDescription,
                fontSize = 14.sp,
                color = Color.Black,
                fontFamily = mediumFont,
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun ProductItem(subMetalDetail: SubMetals) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 0.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(3f)
        ) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${subMetalDetail.submetalName} ",
                fontSize = 14.sp,
                color = Color.Black,
                maxLines = 1,
                fontFamily = mediumFont,
                modifier = Modifier
                    .weight(2f)
                    .padding(4.dp),
                overflow = TextOverflow.Ellipsis // This will show "..." for truncated text
            )
            Text(
                text = " -  ${subMetalDetail.submetalUrduName}",
                fontSize = 16.sp,
                color = Color.Black,
                maxLines = 1,
                fontFamily = mediumFont,
                modifier = Modifier
                    .weight(2f)
                    .padding(4.dp),
                overflow = TextOverflow.Ellipsis // This will show "..." for truncated text
            )
        }
        Box(
            modifier = Modifier
                .background(DarkBlue, shape = RoundedCornerShape(8.dp))
                .padding(8.dp), contentAlignment = Alignment.Center
        ) {
            Text(
                text = subMetalDetail.priceMin + "-" + subMetalDetail.priceMax,
                modifier = Modifier.padding(horizontal = 8.dp),
                fontSize = 12.sp,
                fontFamily = regularFont,
                color = Color.White,
            )
        }
    }
}

@Composable
fun NoProductView(msg: String, color: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = msg, color = color, fontFamily = mediumFont,
            fontSize = 16.sp
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewDashboardScreen() {
    PSP_AndroidTheme {
        DashBoardScreen(
            TextFieldValue(""), "",
            cityList = listOf(),
            listOf(),
            suggestedSearchList = listOf(),
            isRefreshing = false,
            onSearch = {},
            onPullDown = {},
            onSearchClick = {},
            onCityItemClick = {},
        )
    }
}