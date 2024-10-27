package com.example.psrandroid.ui.screen.dashboard

import android.content.Context
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.psp_android.R
import com.example.psrandroid.dto.UpdateLocation
import com.example.psrandroid.navigation.Screen
import com.example.psrandroid.network.isNetworkAvailable
import com.example.psrandroid.response.LocationData
import com.example.psrandroid.response.MetalType
import com.example.psrandroid.response.mockup
import com.example.psrandroid.ui.screen.auth.AuthVM
import com.example.psrandroid.ui.screen.auth.ListDialog
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.LightBlue
import com.example.psrandroid.ui.theme.LightGray
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.ui.theme.PurpleGrey40
import com.example.psrandroid.ui.theme.mediumFont
import com.example.psrandroid.ui.theme.regularFont
import com.example.psrandroid.utils.Utils.convertMillisToDate
import com.example.psrandroid.utils.Utils.getCurrentDate
import com.example.psrandroid.utils.Utils.isValidDate
import com.example.psrandroid.utils.isVisible
import com.example.psrandroid.utils.progressBar
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import es.dmoral.toasty.Toasty
import io.github.rupinderjeet.kprogresshud.KProgressHUD
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(navController: NavController, dashboardVM: DashboardVM, authVM: AuthVM) {
    val context = LocalContext.current
    val progressBar: KProgressHUD = remember { context.progressBar() }
    var isRefreshing by remember { mutableStateOf(false) }
    if (!dashboardVM.isLoading)
        isRefreshing = false

    val locationId = dashboardVM.userPreferences.getLocationList()?.data?.find {
        it.name == dashboardVM.userPreferences.getUserPreference()?.location
    }?.id ?: 0 // get location id
    val currentDate = getCurrentDate() // current date

    progressBar.isVisible(dashboardVM.isLoading)
//    val error = dashboardVM.error
//    if (error.isNotEmpty())
//        Toasty.error(context, error, Toast.LENGTH_SHORT, true).show()
    val locationList = dashboardVM.userPreferences.getLocationList()?.data ?: listOf()
    val suggestedSearchList = dashboardVM.userPreferences.getSuggestedList()?: listOf()

    var metalTypeList: List<MetalType> = listOf()
    var search by rememberSaveable { mutableStateOf("") }
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
    var expandedCity by remember { mutableStateOf(false) }
    val locationData = locationList.map { it.name }
    if (expandedCity) {
        ListDialog(dataList = locationData, onDismiss = { expandedCity = false },
            onConfirm = { locationName ->
                location = locationName
                expandedCity = false
                authVM.updateUserLocation(
                    UpdateLocation(
                        userId = sharedPreferences.getUserPreference()?.userId ?: 0,
                        location = locationName
                    )
                )
            })
    }

    val dashboardData = dashboardVM.dashboardData
    if (isNetworkAvailable(context)) {
        if (dashboardData == null)
            dashboardVM.getDashboardData("$locationId", currentDate)
    } else
        Toasty.error(context, stringResource(id = R.string.network_error), Toast.LENGTH_SHORT, true)
            .show()

    val filteredList = dashboardData?.data?.filter { it.name.contains(search, ignoreCase = true) }
        ?.map { it.metalType }?.flatten() ?: listOf()

    DashBoardScreen(locationList, search, name, location, phone,
        filteredList, isRefreshing,
        suggestedSearchList,
        onSearch = { query ->
            search = query
        }, addProfileClick = { navController.navigate(Screen.AddProfileScreen.route) },
        onFilterText = { date, placeId ->
            dashboardVM.getDashboardData(placeId, date)
        },
        onLocationClick = {
            expandedCity = true
        },
        onPullDown = {
            isRefreshing = true
            if (isNetworkAvailable(context)) {
                if (dashboardData == null)
                    dashboardVM.getDashboardData("$locationId", getCurrentDate())
            } else
                Toasty.error(
                    context,
                    "No internet connection. Please check your network settings.",
                    Toast.LENGTH_SHORT,
                    true
                )
                    .show()

        },
        onSearchClick = {text->
            val isFound = suggestedSearchList.find { it == text}
            if (isFound == null){
                val mutableList = suggestedSearchList.toMutableList()
                mutableList.add(text)
                dashboardVM.userPreferences.saveSuggestedList(mutableList)
            }
        })
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashBoardScreen(
    locationList: List<LocationData>,
    search: String,
    name: String,
    address: String,
    phone: String,
    productList: List<MetalType>,
    isRefreshing: Boolean,
    suggestedSearchList: List<String>,
    onSearchClick: (String) -> Unit,
    onSearch: (String) -> Unit,
    addProfileClick: () -> Unit,
    onLocationClick: () -> Unit,
    onFilterText: (String, String) -> Unit,
    onPullDown: () -> Unit,
) {
    SwipeRefresh(
        state = SwipeRefreshState(isRefreshing),
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
                SearchBar(locationList, suggestedSearchList, search, onSearch = { onSearch(it) },
                    onFilterText = { selectedDate, location ->
                        onFilterText(selectedDate, location)
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
                        text = stringResource(id = R.string.scrap_name),
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
                if (productList.isEmpty())
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchBar(
    locationList: List<LocationData>,
    suggestedSearchList: List<String>,
    search: String,
    onSearchClick: (String) -> Unit,
    onSearch: (String) -> Unit,
    onFilterText: (String, String) -> Unit
) {
    var showFilterDialog by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }
    var expandedDropDown by remember { mutableStateOf(false) }

    if (isFocused)
        expandedDropDown = true

    if (showFilterDialog) {
        FilterDialog(
            locationList,
            onDismissRequest = {
                showFilterDialog = false
            },
            onDateSelected = { date, location ->
                onFilterText(date, location)
                showFilterDialog = false
            })
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(value = search, onValueChange = { value ->
            onSearch(value)
        }, leadingIcon = {
            Icon(
                painterResource(id = R.drawable.search_ic),
                contentDescription = "",
                modifier = Modifier.size(24.dp)
            )
        }, placeholder = {
            Text(
                text = stringResource(id = R.string.search),
                color = Color.Gray,
                fontSize = 12.sp
            )
        },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DarkBlue, // Change focused border color
                unfocusedBorderColor = Color.Transparent, // Change unfocused border color
                focusedLabelColor = Color.Transparent,
                unfocusedLabelColor = Color.Transparent,
                focusedTextColor = DarkBlue,
                unfocusedTextColor = Color.Gray
            ), modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White, shape = RoundedCornerShape(12)
                )
                .clip(RoundedCornerShape(12.dp))
                .padding(2.dp)
                .onFocusChanged { isFocused = it.isFocused },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClick(search)
                }
            )
        )
        // Dropdown menu below the search bar
        DropdownMenu(
            expanded = expandedDropDown && suggestedSearchList.isNotEmpty(),
            onDismissRequest = { expandedDropDown = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            suggestedSearchList.forEach { result ->
                DropdownMenuItem(
                    text = { Text(result) },
                    onClick = {
                        onSearch(result) // Update search bar with selected item
                        expandedDropDown = false // Hide dropdown after selection
                    }
                )
            }
        }
//        Image(
//            painter = painterResource(id = R.drawable.filter_ic),
//            contentDescription = "",
//            Modifier
//                .size(40.dp)
//                .clickable {
////                    showFilterDialog = true
//                }
//        )
//        Image(
//            painter = painterResource(id = R.drawable.bottom_up_list),
//            contentDescription = "",
//            Modifier.size(40.dp)
//        )
    }
}

@Composable
fun ProductList(dashboardData: List<MetalType>) {

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 0.dp)
    ) {
        items(dashboardData.size ?: 0) { index ->
            ProductItem(metalDetail = dashboardData[index], index = index + 1)
            HorizontalDivider()
        }
    }
}

@Composable
fun ProductItem(metalDetail: MetalType, index: Int) {
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
                text = metalDetail.name,
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
                text = "Rs. ${metalDetail.price}",
                modifier = Modifier.padding(horizontal = 8.dp),
                fontSize = 12.sp,
                fontFamily = regularFont,
                color = Color.White,
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FilterDialog(
    locationData: List<LocationData>,
    onDismissRequest: () -> Unit,
    onDateSelected: (String, String) -> Unit
) {
    val locationList = locationData.map { it.name }
    var isClicked by rememberSaveable { mutableStateOf(false) }
    val iconColor by rememberUpdatedState(if (isClicked) LightBlue else PurpleGrey40)
    var selectedDate by rememberSaveable { mutableStateOf("Date") }
    var selectedLocation by rememberSaveable { mutableStateOf("Location") }
    var isDatePickerVisible by rememberSaveable { mutableStateOf(false) }
    var expandedLocation by remember { mutableStateOf(false) }

    if (expandedLocation) {
        ListDialog(dataList = locationList, onDismiss = { expandedLocation = false },
            onConfirm = { locationName ->
                val locationId = locationData.find { it.name == locationName }?.id ?: 0
                selectedLocation = "$locationId"
                expandedLocation = false
            })
    }

    val context = LocalContext.current
    if (isDatePickerVisible) {
        MyDatePickerDialog(
            context,
            onDateSelected = { date ->
                selectedDate = date
                isDatePickerVisible = false
            },
            onDismissRequest = { isDatePickerVisible = false }
        )
    }

    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontFamily = mediumFont,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                HorizontalDivider(thickness = 0.3.dp, color = Color.Gray)

                Spacer(modifier = Modifier.padding(top = 20.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .background(
                            color = LightGray, // Blue for selected, transparent otherwise
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            width = 1.dp,
                            if (isClicked) LightBlue else LightGray,
                        )
                ) {
                    Text(
                        text = selectedLocation,
                        color = if (isClicked) LightBlue else PurpleGrey40,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))  // Add a Spacer to fill remaining space

                    Image(
                        painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clickable {
                                expandedLocation = true
                            },
                        colorFilter = ColorFilter.tint(iconColor),
                    )

                }

                Spacer(modifier = Modifier.padding(top = 20.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .background(
                            color = LightGray, // Blue for selected, transparent otherwise
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            width = 1.dp,
                            if (isClicked) LightBlue else LightGray,
                        )
                ) {
                    Text(
                        text = selectedDate,
                        color = if (isClicked) LightBlue else PurpleGrey40,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))  // Add a Spacer to fill remaining space

                    Image(
                        painter = painterResource(id = R.drawable.calendar_ic),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clickable {
                                isDatePickerVisible = true
                                isClicked = !isClicked
                            },
                        colorFilter = ColorFilter.tint(iconColor),
                    )

                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextButton(
                        onClick = { onDismissRequest() },
                    ) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            color = Color.LightGray,
                            fontSize = 17.sp,
                            fontFamily = regularFont
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = { onDateSelected(selectedDate, selectedLocation) },
                    ) {
                        Text(
                            text = stringResource(id = R.string.ok),
                            color = LightBlue,
                            fontSize = 17.sp,
                            fontFamily = regularFont
                        )
                    }
                }
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(
    context: Context,
    onDateSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val calendar = Calendar.getInstance().apply { add(Calendar.YEAR, 0) }
    val timeInMillis = calendar.timeInMillis

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = timeInMillis,
        initialDisplayedMonthMillis = timeInMillis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                // Check if the date is 18 years ago or earlier
                return utcTimeMillis <= timeInMillis
            }
        }
    )

    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    }
        ?: ""
//        ?: "convertMillisToDate(eighteenYearsAgoInMillis)"// Default to 18 years ago if no date is selected

    DatePickerDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            Button(
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 8.dp, top = 8.dp)
                    .width(100.dp),
                onClick = {
                    if (isValidDate(selectedDate)) {
                        onDateSelected(selectedDate)
                        onDismissRequest()
                    } else
                        Toasty.error(
                            context,
                            "You have entered Invalid date",
                            Toast.LENGTH_SHORT,
                            true
                        ).show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightBlue,
                    contentColor = Color.White
                )

            ) {
                Text(text = stringResource(id = R.string.ok), fontFamily = regularFont)
            }
        },
        dismissButton = {
            Button(
                modifier = Modifier
                    .padding(end = 8.dp, bottom = 8.dp, top = 8.dp)
                    .width(100.dp),
                onClick = {
                    onDismissRequest()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                )
            ) {
                Text(text = stringResource(id = R.string.cancel), fontFamily = regularFont)
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            modifier = Modifier,
            colors = DatePickerDefaults.colors(
                selectedDayContainerColor = LightBlue, // Set your desired color here
                selectedDayContentColor = Color.White // Set content color for the selected day
            )
        )
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
            listOf(LocationData.mockup), "", "", "", "",
            listOf(),
            suggestedSearchList = listOf(),
            isRefreshing = false,
            onSearch = {},
            addProfileClick = {},
            onFilterText = { _, _ ->
            },
            onLocationClick = {},
            onPullDown = {},
            onSearchClick = {})
    }
}