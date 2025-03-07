package com.example.psrandroid.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.unit.sp
import com.example.psp_android.R
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.regularFont
import com.example.psrandroid.utils.PSRNavigationContentPosition

@Composable
fun PermanentNavigationDrawerContent(
    bottomMenuList: List<BottomNavigationItem>,
    selectedDestination: String,
    navigationContentPosition: PSRNavigationContentPosition,
    navigateToTopLevelDestination: (BottomNavigationItem) -> Unit,
    onLogOut: () -> Unit
) {
    //Drawer which show on Tablet or large device on landscape mode
    PermanentDrawerSheet(
        modifier = Modifier.sizeIn(minWidth = 200.dp, maxWidth = 250.dp),
        drawerContainerColor = colorResource(id = R.color.white),
    ) {
        Layout(
            modifier = Modifier.padding(16.dp),
            content = {
                Column(
                    modifier = Modifier.layoutId(LayoutType.HEADER),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp),
                        text = stringResource(id = R.string.app_name).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = DarkBlue
                    )
                    ExtendedFloatingActionButton(
                        onClick = { onLogOut() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        containerColor = Color.White,
                        contentColor = DarkBlue
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.logout),
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 16.dp),
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .layoutId(LayoutType.CONTENT)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    bottomMenuList.forEach { psrDestination ->
                        NavigationDrawerItem(
                            selected = selectedDestination == psrDestination.route,
                            label = {
                                Text(
                                    text = psrDestination.name,
                                    color = colorResource(id = R.color.black),
                                    fontFamily = regularFont,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = psrDestination.icon),
                                    contentDescription = "",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = Color.White,
                                unselectedContainerColor = DarkBlue
                            ),
                            onClick = { navigateToTopLevelDestination(psrDestination) }
                        )
                    }
                }
            },
            measurePolicy = navigationMeasurePolicy(navigationContentPosition)
        )
    }
}

fun navigationMeasurePolicy(
    navigationContentPosition: PSRNavigationContentPosition,
): MeasurePolicy {
    return MeasurePolicy { measure, constraints ->
        lateinit var headerMeasurable: Measurable
        lateinit var contentMeasurable: Measurable
        measure.forEach {
            when (it.layoutId) {
                LayoutType.HEADER -> headerMeasurable = it
                LayoutType.CONTENT -> contentMeasurable = it
                else -> error("Unknown layoutId encountered!")
            }
        }

        val headerPlaceable = headerMeasurable.measure(constraints)
        val contentPlaceable = contentMeasurable.measure(
            constraints.offset(vertical = -headerPlaceable.height)
        )
        layout(constraints.maxWidth, constraints.maxHeight) {
            // Place the header, this goes at the top
            headerPlaceable.placeRelative(0, 0)

            // Determine how much space is not taken up by the content
            val nonContentVerticalSpace = constraints.maxHeight - contentPlaceable.height

            val contentPlaceableY = when (navigationContentPosition) {
                // Figure out the place we want to place the content, with respect to the
                // parent (ignoring the header for now)
                PSRNavigationContentPosition.TOP -> 0
                PSRNavigationContentPosition.CENTER -> nonContentVerticalSpace / 2
            }
                // And finally, make sure we don't overlap with the header.
                .coerceAtLeast(headerPlaceable.height)

            contentPlaceable.placeRelative(0, contentPlaceableY)
        }
    }
}

@Composable
fun PSRBottomNavigationBar(
    bottomMenuList: List<BottomNavigationItem>,
    selectedDestination: String,
    navigateToTopLevelDestination: (BottomNavigationItem) -> Unit
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = colorResource(id = R.color.white),
        contentColor = DarkBlue
    ) {
        bottomMenuList.forEach { fitmeBottomDestination ->
            NavigationBarItem(
                selected = selectedDestination == fitmeBottomDestination.route,
                onClick = { navigateToTopLevelDestination(fitmeBottomDestination) },
                icon = {
                    Icon(
                        painter = painterResource(id = fitmeBottomDestination.icon),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = fitmeBottomDestination.name,
                        fontSize = 12.sp,
                        fontFamily = regularFont,
                        lineHeight = 2.sp,
                        textAlign = TextAlign.Center
                    )
                },
                colors = NavigationBarItemColors(
                    selectedIndicatorColor = Color.Transparent,
                    selectedIconColor = DarkBlue,
                    selectedTextColor = DarkBlue,
                    unselectedIconColor = colorResource(id = R.color.text_grey),
                    unselectedTextColor = colorResource(id = R.color.text_grey),
                    disabledIconColor = colorResource(id = R.color.text_grey),
                    disabledTextColor = colorResource(id = R.color.text_grey)
                )
            )
        }
    }
}

enum class LayoutType {
    HEADER, CONTENT
}