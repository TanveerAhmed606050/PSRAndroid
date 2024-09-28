package com.example.psrandroid.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.psrandroid.response.User
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        const val SHARED_PREF_NAME = "user_credential_Prefer"
        const val USER_DATA = "user_login_data"
        const val IS_FIRST_LAUNCH = "isFirstLaunch"
    }

    var isFirstLaunch: Boolean
        get() = sharedPreferences.getBoolean(IS_FIRST_LAUNCH, true)
        set(value) = sharedPreferences.edit { putBoolean(IS_FIRST_LAUNCH, value) }
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveUserPreference(loginData: User) {
        sharedPreferences.edit {
            putString(USER_DATA, gson.toJson(loginData))
        }
    }

    fun getUserPreference(): User? {
        val json = sharedPreferences.getString(USER_DATA, null)
        return try {
            gson.fromJson(json, User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun clearStorage() = sharedPreferences.edit().clear().apply()

}