package com.example.psrandroid.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object Utils {
    // Function to load cities from JSON file in assets
    fun isValidPhone(phoneNumber: String): String {
        val phoneNumberPattern = Regex("^[+]?[0-9]{10,13}\$")
        return if (phoneNumberPattern.matches(phoneNumber)) "" else "Enter valid number"
    }

    //    fun isValidEmail(email: String): Boolean {
//        val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
//        return email.matches(emailRegex)
//    }
    fun isValidPassword(password: String): String {
        return if (password.isEmpty())
            "Password fields must not be empty."
        else if (password.trim().length < 8)
            "Password must be at least 8 characters long."
        else ""
//    val passwordPattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$")
//    val result = passwordPattern.matches(password)
//    return when (!result) {
//        true -> {
//            when {
//                !password.contains("[A-Z]".toRegex()) -> "Password must contain at least one uppercase letter (A-Z)."
//                !password.contains("[a-z]".toRegex()) -> "Password must contain at least one lowercase letter (A-Z)."
//                !password.contains("\\d".toRegex()) -> "Password must contain at least one digit (0â€“9)."
//                !password.contains("[@#$%^&+=!]".toRegex()) -> "Password must contain at least one special character (@#$%^&+=!)."
//                else ->
//                    "Password must be at least 8 characters long."
//            }
//        }
//
//        else -> ""
//    }
    }

    fun isValidText(text: String): String {
        return if (text.trim().replace("\n", "").matches(Regex("^[^0-9]+$"))
            && text.length >= 3
        )
            ""
        else "Enter valid name"
    }

    fun loadCitiesFromAssets(context: Context): List<String> {
        val inputStream = context.assets.open("cities.json")
        val reader = InputStreamReader(inputStream)
        val type = object : TypeToken<Map<String, List<String>>>() {}.type
        val jsonMap = Gson().fromJson<Map<String, List<String>>>(reader, type)
        return jsonMap["cities"] ?: emptyList()
    }

    fun convertImageFileToBase64(uri: Uri, contentResolver: ContentResolver): String {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun isValidDate(date: String): Boolean {
        return try {
            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            sdf.isLenient = false
            sdf.parse(date)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun convertMillisToDate(millis: Long): String {
        val formatter =
            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()) // Fix: "MM" for month, "dd" for day
        return formatter.format(Date(millis))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(): String {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return today.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDateDisplay(dateString: String):String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
        val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)
        val date = LocalDate.parse(dateString, inputFormatter)
        return date.format(outputFormatter)
    }
}