package com.example.psrandroid.ui.screen.dashboard

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.psp_android.R
import com.example.psrandroid.dto.UpdateLocation
import com.example.psrandroid.navigation.Screen
import com.example.psrandroid.network.isNetworkAvailable
import com.example.psrandroid.response.MetalData
import com.example.psrandroid.response.SubMetalData
import com.example.psrandroid.ui.screen.auth.AuthVM
import com.example.psrandroid.ui.screen.auth.ListDialog
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.LightBlue
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.ui.theme.mediumFont
import com.example.psrandroid.ui.theme.regularFont
import com.example.psrandroid.utils.isVisible
import com.example.psrandroid.utils.progressBar
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import es.dmoral.toasty.Toasty
import io.github.rupinderjeet.kprogresshud.KProgressHUD

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(navController: NavController, dashboardVM: DashboardVM, authVM: AuthVM) {
    val context = LocalContext.current
    val progressBar: KProgressHUD = remember { context.progressBar() }
    var isRefreshing by remember { mutableStateOf(false) }
    if (!dashboardVM.isLoading)
        isRefreshing = false

    var locationId = dashboardVM.userPreferences.getLocationList()?.data?.find {
        it.name.equals(dashboardVM.userPreferences.getUserPreference()?.location, ignoreCase = true)
    }?.id ?: 0 // get location id

    progressBar.isVisible(dashboardVM.isLoading)
    val locationList = dashboardVM.userPreferences.getLocationList()?.data ?: listOf()

    var search by remember { mutableStateOf(TextFieldValue(dashboardVM.userPreferences.lastSearchMetal)) }
    val sharedPreferences = dashboardVM.userPreferences
    val name by remember { mutableStateOf(sharedPreferences.getUserPreference()?.name ?: "Ahmed") }

    var location by remember {
        mutableStateOf(
            sharedPreferences.getUserPreference()?.location ?: "Lahore"
        )
    }
    val phone by remember {
        mutableStateOf(
            sharedPreferences.getUserPreference()?.phone ?: "+92 30515151"
        )
    }
    val noInternetMessage = stringResource(id = R.string.network_error)
    var expandedCity by remember { mutableStateOf(false) }
    val locationData = locationList.map { it.name }
    if (expandedCity) {
        ListDialog(dataList = locationData, onDismiss = { expandedCity = false },
            onConfirm = { locationName ->
                location = locationName
                expandedCity = false
                authVM.updateUserLocation(
                    UpdateLocation(
                        userId = sharedPreferences.getUserPreference()?.id ?: 0,
                        location = locationName
                    )
                )
                locationId = dashboardVM.userPreferences.getLocationList()?.data?.find {
                    it.name.equals(locationName, ignoreCase = true)
                }?.id ?: 0
            })
    }

    LaunchedEffect(locationId) {
        dashboardVM.getSubMetals("$locationId", search.text)
    }
    val subMetalData = dashboardVM.subMetalData
    val mainMetalsData = dashboardVM.mainMetalData
    val suggestedSearchList = dashboardVM.suggestMainMetals

    if (isNetworkAvailable(context)) {
        LaunchedEffect(key1 = Unit) {
            dashboardVM.getSubMetals("$locationId", search.text)
        }
    } else
        Toasty.error(context, noInternetMessage, Toast.LENGTH_SHORT, true)
            .show()

    if (mainMetalsData == null) {
        dashboardVM.getMainMetals("$locationId", "")
    }

    DashBoardScreen(search, name, location, phone,
        subMetalData?.data, isRefreshing,
        suggestedSearchList,
        onSearch = { query ->
            search = query
            dashboardVM.searchMainMetals(search.text)
        }, addProfileClick = { navController.navigate(Screen.AddProfileScreen.route) },
        onLocationClick = {
            expandedCity = true
        },
        onPullDown = {
            isRefreshing = true
            if (isNetworkAvailable(context)) {
                if (subMetalData == null)
                    dashboardVM.getSubMetals("$locationId", search.text)
            } else
                Toasty.error(
                    context,
                    noInternetMessage,
                    Toast.LENGTH_SHORT,
                    true
                )
                    .show()

        },
        onSearchClick = { searchText ->
            search = searchText
            dashboardVM.userPreferences.lastSearchMetal = searchText.text
            if (isNetworkAvailable(context)) {
                dashboardVM.getSubMetals("$locationId", searchText.text)
            } else
                Toasty.error(
                    context, noInternetMessage, Toast.LENGTH_SHORT, true
                ).show()
        })
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashBoardScreen(
    search: TextFieldValue,
    name: String,
    address: String,
    phone: String,
    productList: List<SubMetalData>?,
    isRefreshing: Boolean,
    suggestedSearchList: List<MetalData>?,
    onSearchClick: (TextFieldValue) -> Unit,
    onSearch: (TextFieldValue) -> Unit,
    addProfileClick: () -> Unit,
    onLocationClick: () -> Unit,
    onPullDown: () -> Unit,
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
                .background(Color(0xFFEEF1F4)) // Background color
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 100.dp)
            ) {
                // Header section
                HeaderSection(name, address, phone, addProfileClick = {
                    addProfileClick()
                },
                    onLocationClick = { onLocationClick() })

                Spacer(modifier = Modifier.height(0.dp))
                // Search bar
                SearchBar(suggestedSearchList, search,
                    onSearch = {
                        onSearch(it)
                    },
                    onSearchClick = { value ->
                        onSearchClick(value)
                    })

                Spacer(modifier = Modifier.height(0.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${search.text} Scrap",
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
                if (productList?.isEmpty() != false)
                    NoProductView()
                else
                    ProductList(productList)
            }
        }
    }
}

@Composable
fun HeaderSection(
    name: String, address: String, phone: String,
    addProfileClick: () -> Unit,
    onLocationClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkBlue)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .align(Alignment.CenterStart)
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.dashboard),
                    fontSize = 16.sp,
                    fontFamily = mediumFont,
                    color = Color.White
                )
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.location_ic),
                    contentDescription = "",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onLocationClick() }
                )
                Text(
                    text = " | $address", color = Color.White, fontSize = 16.sp,
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .background(
                        color = LightBlue, shape = RoundedCornerShape(12.dp)
                    )
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(start = 0.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.steel_ic), // Replace with actual image resource
                    contentDescription = "",
                    modifier = Modifier
                        .width(100.dp)
                        .fillMaxHeight()
                        .padding(start = 8.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {

                    UserDetailItem(imageId = R.drawable.profile_ic, text = name)
                    HorizontalDivider(color = Color.White)
                    UserDetailItem(imageId = R.drawable.location_ic, text = address)
                    HorizontalDivider(color = Color.White)
                    UserDetailItem(imageId = R.drawable.phone_ic, text = phone)

                }
            }
        }
    }
}

@Composable
fun UserDetailItem(imageId: Int, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = "",
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.padding(end = 12.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchBar(
    suggestedSearchList: List<MetalData>?,
    search: TextFieldValue,
    onSearchClick: (TextFieldValue) -> Unit,
    onSearch: (TextFieldValue) -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) }
    var expandedDropDown by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Observe the search text and update dropdown state based on focus and list availability
    LaunchedEffect(isFocused, suggestedSearchList) {
        expandedDropDown = isFocused && (suggestedSearchList?.isNotEmpty() == true)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
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
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.search_ic),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp)
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search),
                        color = Color.Gray,
                        fontSize = 12.sp
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
                    .background(color = Color.White, shape = RoundedCornerShape(12))
                    .clip(RoundedCornerShape(12.dp))
                    .padding(2.dp)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    }
                    .menuAnchor(type = MenuAnchorType.PrimaryEditable),
                keyboardOptions = KeyboardOptions.Default.copy(
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
                                    selection = TextRange(mainMetal.name.length)
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
fun ProductList(dashboardData: List<SubMetalData>?) {

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 0.dp)
    ) {
        items(dashboardData?.size ?: 0) { index ->
            ProductItem(metalDetail = dashboardData?.get(index), index = index + 1)
            HorizontalDivider()
        }
    }
}

@Composable
fun ProductItem(metalDetail: SubMetalData?, index: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 20.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp)),

        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(3f)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(LightBlue, DarkBlue)
                        ), shape = RoundedCornerShape(6.dp)
                    )
                    .weight(0.5f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = String.format("%02d", index),
                    color = Color.White,
                    fontFamily = regularFont,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${metalDetail?.submetal_name}",
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1,
                fontFamily = regularFont,
                modifier = Modifier.weight(2f),
                overflow = TextOverflow.Ellipsis // This will show "..." for truncated text
            )
        }
        Box(
            modifier = Modifier
                .background(DarkBlue, shape = RoundedCornerShape(8.dp))
                .padding(8.dp), contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Rs.${metalDetail?.price}",
                modifier = Modifier.padding(horizontal = 8.dp),
                fontSize = 12.sp,
                fontFamily = regularFont,
                color = Color.White,
            )
        }
    }
}

@Composable
fun NoProductView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No metal found", color = LightBlue, fontFamily = mediumFont,
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
            TextFieldValue(""), "", "", "",
            listOf(),
            suggestedSearchList = listOf(),
            isRefreshing = false,
            onSearch = {},
            addProfileClick = {},
            onLocationClick = {},
            onPullDown = {},
            onSearchClick = {})
    }
}