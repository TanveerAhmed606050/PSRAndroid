package com.example.psrandroid.ui.screen.dashboard

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.psp_android.R
import com.example.psrandroid.navigation.Screen
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.LightBlue
import com.example.psrandroid.ui.theme.PSP_AndroidTheme

@Composable
fun DashboardScreen(navController: NavController, dashboardVM: DashboardVM) {
    var search by rememberSaveable { mutableStateOf("") }
    val sharedPreferences = dashboardVM.userPreferences
    val name by remember { mutableStateOf(sharedPreferences.getUserPreference()?.name ?: "Ahmed") }
    val email by remember {
        mutableStateOf(
            sharedPreferences.getUserPreference()?.email ?: "jahanshah43@gmail.com"
        )
    }
    val address by remember {
        mutableStateOf("Lalpur, Lahore"
        )
    }
    val phone by remember {
        mutableStateOf(
            sharedPreferences.getUserPreference()?.phone ?: "+92 30515151"
        )
    }

    DashBoardScreen(search, name, email, address, phone, onSearch = {
        search = it
    }, addProfileClick = { navController.navigate(Screen.AddProfileScreen.route) })
}

@Composable
fun DashBoardScreen(
    search: String,
    name: String,
    email: String,
    address: String,
    phone: String,
    onSearch: (String) -> Unit,
    addProfileClick: () -> Unit
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
            HeaderSection(name, email, address, phone)

            Spacer(modifier = Modifier.height(0.dp))
            // Search bar
            SearchBar(search, onSearch = { onSearch(it) })

            Spacer(modifier = Modifier.height(0.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.scrap_name),
                    color = LightBlue,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.price_kg),
                    color = LightBlue,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 12.dp)
                )
            }
            // List of scrap products
            ProductList()
        }
        // Bottom action button
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "Add Profile",
                modifier = Modifier.padding(bottom = 72.dp),
                color = LightBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
            Image(painter = painterResource(id = R.drawable.plus_bottom),
                contentDescription = null,
                modifier = Modifier.clickable { addProfileClick() })
        }
    }

}

@Composable
fun HeaderSection(name: String, email: String, address: String, phone: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkBlue)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterStart)
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.dashboard),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.location_ic),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = " | Lahore", color = Color.White, fontSize = 16.sp,
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .background(
                        color = LightBlue, shape = RoundedCornerShape(12.dp)
                    )
                    .fillMaxWidth()
                    .height(150.dp)
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_settings_24),
                            contentDescription = "",
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.TopEnd)
                        )
                    }
                    UserDetailItem(imageId = R.drawable.profile_ic, text = name)
                    HorizontalDivider(color = Color.White)
                    UserDetailItem(
                        imageId = R.drawable.message_ic, text = email
                    )
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
            .padding(0.dp),
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

@Composable
fun SearchBar(search: String, onSearch: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(value = search, onValueChange = { value ->
            onSearch(value)
        }, leadingIcon = {
            Icon(
                painterResource(id = R.drawable.search_ic),
                contentDescription = "Search Icon",
                modifier = Modifier.size(30.dp)
            )
        }, placeholder = {
            Text(text = stringResource(id = R.string.search), color = Color.Gray)
        }, colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = DarkBlue, // Change focused border color
            unfocusedBorderColor = Color.Transparent, // Change unfocused border color
            focusedLabelColor = Color.Transparent,
            unfocusedLabelColor = Color.Transparent,
            focusedTextColor = DarkBlue,
            unfocusedTextColor = Color.Gray
        ), modifier = Modifier
            .background(
                color = Color.White, shape = RoundedCornerShape(12)
            )
            .clip(RoundedCornerShape(12.dp))
            .padding(2.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.filter_ic),
            contentDescription = "",
            Modifier.size(40.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.bottom_up_list),
            contentDescription = "",
            Modifier.size(40.dp)
        )
    }
}

@Composable
fun ProductList() {
    val products = listOf(
        "Battery Scrap Rate",
        "Steel Scrap Rate",
        "Brass Scrap Rate",
        "Aluminium Scrap Price",
        "Silver Scrap Price",
        "Nigar Scrap Ratesv",
        "Battery Scrap Rate",
        "Steel Scrap Rate",
        "Brass Scrap Rate",
        "Aluminium Scrap Price",
        "Silver Scrap Price",
        "Nigar Scrap Ratesv"
    )

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 0.dp)
    ) {
        items(products.size) { index ->
            ProductItem(productName = products[index], index = index + 1)
            HorizontalDivider()
        }
    }
}

@Composable
fun ProductItem(productName: String, index: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 16.dp)
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
                    .background(brush = Brush.verticalGradient(
                        colors = listOf(LightBlue, DarkBlue)
                    ), shape = RoundedCornerShape(6.dp))
                    .weight(0.5f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = String.format("%02d", index),
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = productName,
                fontSize = 16.sp,
                color = Color.Gray,
                maxLines = 1,
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
                text = "Rs. 420 to Rs. 445",
                modifier = Modifier.padding(horizontal = 8.dp),
                fontSize = 12.sp,
                color = Color.White,
            )
        }
    }
}

@Preview
@Composable
fun PreviewDashboardScreen() {
    PSP_AndroidTheme {
        DashBoardScreen("", "", "", "", "", onSearch = {},
            addProfileClick = {})
    }
}