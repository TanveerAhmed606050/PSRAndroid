package com.example.psrandroid.ui.commonViews
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.psrandroid.network.isNetworkAvailable
import kotlinx.coroutines.delay


@Composable
fun NetworkStatus() {
    val context = LocalContext.current
    var isNetworkAvailable by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            isNetworkAvailable = isNetworkAvailable(context)
            delay(2000) // Check network status every 2 seconds
        }
    }

    if (!isNetworkAvailable) {
        AlertDialog(
            onDismissRequest = { /* Don't dismiss */ },
            title = { Text(text = "Network Error") },
            text = { Text("No internet connection. Please check your network settings.") },
            confirmButton = {
                Button(onClick = { /* Optionally retry connection */ }) {
                    Text("Retry")
                }
            }
        )
    }
}
