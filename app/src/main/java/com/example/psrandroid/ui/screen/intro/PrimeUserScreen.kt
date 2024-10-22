package com.example.psrandroid.ui.screen.intro

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.psp_android.R
import com.example.psrandroid.ui.commonViews.MyAsyncImage
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.LightBlue
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.ui.theme.mediumFont
import com.example.psrandroid.ui.theme.regularFont

@Composable
fun PrimeUserScreen(navController: NavController) {
    PrimeUserScreen()
}

@Composable
fun PrimeUserScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(LightBlue, DarkBlue)))
            .padding(bottom = 90.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Spacer(modifier = Modifier.height(10.dp))
            Spacer(modifier = Modifier.statusBarsPadding())
            Text(
                text = stringResource(id = R.string.prime_user), fontSize = 16.sp,
                fontFamily = mediumFont,
                color = Color.White
            )
            Spacer(modifier = Modifier.padding(top = 10.dp))
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 0.dp)
            ) {
                items(4) { index ->
                    UserItemData()

                }
            }
        }
    }
}

@Composable
fun UserItemData() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(vertical = 8.dp, horizontal = 20.dp)
            .background(color = Color.White, shape = RoundedCornerShape(12.dp))
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            MyAsyncImage(imageUrl = "", 45.dp, true)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 0.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.padding(end = 8.dp, start = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Name",
                    color = Color.Black,
                    fontFamily = regularFont,
                    fontSize = 12.sp,
                )
                Text(
                    text = "Business Name",
                    fontSize = 12.sp,
                    fontFamily = regularFont,
                    color = Color.Black,
                )
                Text(
                    text = "Phone Number",
                    fontSize = 12.sp,
                    color = Color.Black,
                    fontFamily = regularFont,
                )
                Text(
                    text = "Address",
                    fontFamily = regularFont,
                    color = Color.Black,
                    fontSize = 12.sp,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp, start = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Ali",
                    color = Color.Black,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis, // This will show "..." for truncated text
                    fontFamily = mediumFont,
                )
                Text(
                    text = "Ali's Steel mill",
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis, // This will show "..." for truncated text
                    fontSize = 14.sp,
                    fontFamily = mediumFont,
                )
                Text(
                    text = "+923451234567",
                    color = Color.Black,
                    fontFamily = mediumFont,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis, // This will show "..." for truncated text
                    fontSize = 14.sp,
                )
                Text(
                    text = "Abc Market, Lahore",
                    color = Color.Black,
                    fontFamily = mediumFont,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis, // This will show "..." for truncated text
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
            }
        }
    }
}

@Preview
@Composable
fun PrimeUserScreenPreview() {
    PSP_AndroidTheme {
        PrimeUserScreen()
    }
}