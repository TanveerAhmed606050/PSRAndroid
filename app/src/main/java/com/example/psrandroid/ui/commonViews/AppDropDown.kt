package com.example.psrandroid.ui.commonViews

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SimpleDropDownMenu(
    itemsIngredient: List<String>,
    expanded: Boolean,
    onDismissSelected: (Boolean) -> Unit,
    onOptionSelected: (String) -> Unit
) {
    // Use BoxWithConstraints to access the screen width
    BoxWithConstraints {
        val screenWidthPx = with(LocalDensity.current) { maxWidth }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDismissSelected(false) },
            // Use offset to align dropdown to the right
            offset = DpOffset((screenWidthPx - 200.dp), 0.dp) // Adjust 200 to match dropdown width
        ) {
            itemsIngredient.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item, fontSize = 16.sp, fontWeight = FontWeight.Normal) },
                    onClick = {
                        onOptionSelected(item)
                        onDismissSelected(false)
                    }
                )
            }
        }
    }
}

