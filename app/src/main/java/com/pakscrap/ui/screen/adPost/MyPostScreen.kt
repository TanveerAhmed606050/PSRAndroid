package com.pakscrap.ui.screen.adPost

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SmallFloatingActionButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.pakscrap.R
import com.pakscrap.navigation.Screen
import com.pakscrap.network.isNetworkAvailable
import com.pakscrap.ui.commonViews.LoadingDialog
import com.pakscrap.ui.screen.adPost.models.AdPostDto
import com.pakscrap.ui.screen.adPost.models.AdsData
import com.pakscrap.ui.screen.adPost.models.mockup
import com.pakscrap.ui.screen.rate.NoProductView
import com.pakscrap.ui.theme.AppBG
import com.pakscrap.ui.theme.DarkBlue
import com.pakscrap.ui.theme.LightRed40
import com.pakscrap.ui.theme.PSP_AndroidTheme
import com.pakscrap.ui.theme.boldFont
import com.pakscrap.ui.theme.regularFont
import com.pakscrap.utils.Constant
import com.pakscrap.utils.Utils.isRtlLocale
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.flow.flowOf
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyPostScreen(navController: NavController, adPostVM: AdPostVM) {
    val context = LocalContext.current
    val allAdsData = adPostVM.allAdsData.collectAsLazyPagingItems()
    var showProgress by remember { mutableStateOf(false) }
    showProgress = adPostVM.isLoading
    if (showProgress)
        LoadingDialog()
    val connectivityError = stringResource(id = R.string.network_error)
    LaunchedEffect(Unit) {
        if (isNetworkAvailable(context))
            adPostVM.getAdsByUserid(
                AdPostDto(
                    userId = "${adPostVM.userPreferences.getUserData()?.id}",
                    perPage = "10",
                    page = "1"
                )
            )
        else
            Toasty.error(context, connectivityError, Toast.LENGTH_SHORT, false)
                .show()
    }
    MyPostScreen(
        adsData = allAdsData,
        onPlusIconClick = {
            navController.navigate(Screen.AdScreen.route)
        },
        onAdsClick = { adData ->
            val adDataJson = Gson().toJson(adData)
            val encodedJson = Uri.encode(adDataJson)
            val isMyAd = true
            navController.navigate(Screen.AdDetailScreen.route + "Details/$encodedJson/$isMyAd")
        })
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyPostScreen(
    adsData: LazyPagingItems<AdsData>,
    onPlusIconClick: () -> Unit,
    onAdsClick: (AdsData) -> Unit,
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
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.my_post), fontSize = 16.sp,
                fontFamily = boldFont,
                color = DarkBlue,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))
            if (adsData.loadState.refresh is LoadState.Loading ||
                adsData.loadState.append is LoadState.Loading
            )
                LinearProgressIndicator()
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
                            isMyAd = true, modifier = Modifier,
                            onAdsClick = { onAdsClick(it) }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        if (index + 1 == adsData.itemCount)
                            Spacer(modifier = Modifier.height(120.dp))
                    }
                }
            }
        }
        SmallFloatingActionButton(
            modifier = Modifier
                .padding(bottom = 120.dp, end = 16.dp)
                .align(Alignment.BottomEnd),
            onClick = {
                onPlusIconClick()
            },
            containerColor = AppBG
        ) {
            Icon(
                painter = painterResource(id = R.drawable.add_post_ic),
                contentDescription = "",
                tint = DarkBlue,
                modifier = Modifier.size(25.dp)
            )
        }

    }
}

@Composable
fun AdsItemsView(
    adData: AdsData,
    isMyAd: Boolean,
    modifier: Modifier,
    onAdsClick: (AdsData) -> Unit,
) {
    Card(
        modifier = modifier
            .height(150.dp)
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(10.dp))
            .clickable { onAdsClick(adData) },
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color.White),
            ) {
                Card(
                    modifier = Modifier
                        .height(150.dp)
                        .width(150.dp)
                        .padding(1.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(DarkBlue),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Box(modifier = Modifier.padding(1.dp)) {
                        AsyncImage(
                            model = if (adData.photos.isNotEmpty()) {
                                Constant.MEDIA_BASE_URL + adData.photos[0]
                            } else adData.photos, contentDescription = "",
                            error = painterResource(id = R.drawable.demo_scrap),
                            placeholder = painterResource(id = R.drawable.demo_scrap),
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_photo_24),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(start = 10.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start,
                                text = adData.photos.size.toString(),
                                fontSize = 14.sp,
                                fontFamily = boldFont,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        text = adData.metalName,
                        fontSize = 16.sp,
                        fontFamily = boldFont,
                        color = Color.DarkGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis // This will show "..." for truncated text
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        text = "PKR ${adData.price}",
                        fontSize = 14.sp,
                        fontFamily = regularFont,
                        color = Color.DarkGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        text = adData.city,
                        fontSize = 14.sp,
                        fontFamily = regularFont,
                        color = Color.DarkGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = adData.description,
                        fontSize = 12.sp,
                        fontFamily = regularFont,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
            if (isMyAd) {
                Text(
                    text = adData.approvalStatus,
                    color = if (adData.approvalStatus == "pending") Color.Black else Color.White,
                    fontFamily = boldFont,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .background(
                            color = when (adData.approvalStatus) {
                                "rejected" -> LightRed40
                                "pending" -> Color.Yellow.copy(
                                    0.4f
                                )

                                else -> DarkBlue
                            },
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(4.dp)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchBar(
    search: TextFieldValue,
    onSearchClick: (TextFieldValue) -> Unit,
) {
    val currentLocale = Locale.getDefault()
    val isRtl = isRtlLocale(currentLocale)
    var isFocused by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = search,
            onValueChange = { value ->
                onSearchClick(value.copy(selection = TextRange(value.text.length)))
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
                },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    onSearchClick(search)
                },
                onDone = {
                    keyboardController?.hide()
                    onSearchClick(search)
                },
                onGo = {
                    keyboardController?.hide()
                    onSearchClick(search)
                },
                onNext = {
                    keyboardController?.hide()
                    onSearchClick(search)
                }
            )
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun HomeScreenPreview() {
    PSP_AndroidTheme {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            MyPostScreen(adsData = flowOf(PagingData.from(listOf(AdsData.mockup))).collectAsLazyPagingItems(),
                onPlusIconClick = {},
                onAdsClick = {})
        }
    }
}