package com.pakscrap.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.core.view.WindowCompat
import com.pakscrap.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Locale

object Utils {
    // Function to load cities from JSON file in assets
    fun isValidPhone(phoneNumber: String): String {
        val phoneNumberPattern = Regex("^[+]?[0-9]{10}\$")
        return if (phoneNumberPattern.matches(phoneNumber)) "" else "Enter valid number"
    }

    fun isValidPassword(password: String): String {
        return if (password.isEmpty())
            "Password fields must not be empty."
        else if (password.trim().length < 8)
            "Password must be at least 8 characters long."
        else ""
    }

    fun isValidText(text: String): String {
        return if (text.trim().replace("\n", "").matches(Regex("^[^0-9]+$"))
            && text.length >= 3
        )
            ""
        else "Enter valid name"
    }

    fun convertImageFileToBase64(uri: Uri, contentResolver: ContentResolver): String {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun uriToFile(context: Context, uri: Uri): File {
        val contentResolver = context.contentResolver
        val tempFile = File(
            context.cacheDir,
            "${System.currentTimeMillis()}.jpg"
        ) // Change extension based on your API requirement

        contentResolver.openInputStream(uri)?.use { inputStream ->
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return tempFile
    }

    fun createMultipartBodyPart(file: File, name: String): MultipartBody.Part {
        val requestBody =
            file.asRequestBody("image/*".toMediaTypeOrNull()) // Change MIME type as needed
        return MultipartBody.Part.createFormData(name, file.name, requestBody)
    }

    fun isRtlLocale(locale: Locale): Boolean {
        val rtlLanguages = listOf("ur") // List of RTL languages
        return rtlLanguages.contains(locale.language)
    }

    fun actionBar(context: Activity) {
        val window = context.window
        // Set the status bar background color to white
        window.statusBarColor = context.resources.getColor(R.color.black, context.theme)

        // Change the status bar icons to black for better visibility
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }
    }

    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date ?: "")
    }
}