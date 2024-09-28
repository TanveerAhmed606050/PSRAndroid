package com.example.psrandroid.ui.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.psp_android.R
import com.example.psrandroid.navigation.Screen
import com.example.psrandroid.ui.commonViews.Header
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.utils.Utils.loadCitiesFromAssets

@Composable
fun CityScreen(navController: NavController) {
    var city by remember { mutableStateOf("City") }
    CityScreen(backClick = { navController.popBackStack() }, city = city,
        onSelectedCity = {
            city = it
        },
        nextClick = {
            navController.navigate(Screen.LoginScreen.route)
        })
}

@Composable
fun CityScreen(
    backClick: () -> Unit, city: String,
    onSelectedCity: (String) -> Unit,
    nextClick: () -> Unit
) {
    // Load cities from JSON in assets
    val context = LocalContext.current
    val cities = remember { loadCitiesFromAssets(context) }
    var expandedCity by remember { mutableStateOf(false) }
//    val list = listOf("Abc", "DER")
    if (expandedCity) {
        ListDialog(dataList = cities, onDismiss = { expandedCity = false },
            onConfirm = {
                onSelectedCity(it)
                expandedCity = false
            })
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.lang_city_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop, // Adjust this as needed
            modifier = Modifier.fillMaxSize(),
        )
        Column(modifier = Modifier.padding(horizontal = 4.dp)) {
            Spacer(modifier = Modifier.height(36.dp))
            Header(
                modifier = null,
                stringResource(id = R.string.enter_city),
                backClick = { backClick() })
            Spacer(modifier = Modifier.height(100.dp))
            Column(modifier = Modifier.padding(16.dp)) {

                HorizontalDivider(color = Color.White, thickness = 2.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedCity = true }
                        .height(45.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.location_ic),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .size(30.dp)
                    )
                    Text(
                        text = city,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 0.dp)
                    )
                }
                HorizontalDivider(color = Color.White, thickness = 2.dp)
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .clickable { nextClick() }
        ) {
            Row(
                modifier = Modifier
                    .background(color = Color.Blue, shape = CircleShape)
                    .size(50.dp)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_navigate_next_24),
                    contentDescription = null
                )
            }

        }
    }
}

@Composable
fun ListDialog(
    dataList: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() }, properties = DialogProperties(
            dismissOnBackPress = true, dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier
                .fillMaxWidth() // Ensures column takes the full width
                .verticalScroll(rememberScrollState())) {
                // List of cities from assets as clickable items
                dataList.forEach { city ->
                    Text(
                        text = city,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onConfirm(city)
                            }
                            .padding(16.dp),
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )

                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewCityScreen() {
    PSP_AndroidTheme {
        CityScreen(backClick = {}, "Lahore", onSelectedCity = {},
            nextClick = {})
    }
}