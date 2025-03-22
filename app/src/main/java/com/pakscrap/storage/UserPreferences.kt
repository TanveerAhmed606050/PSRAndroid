package com.pakscrap.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.pakscrap.response.LocationResponse
import com.google.gson.Gson
import com.pakscrap.ui.screen.auth.models.AuthData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        const val SHARED_PREF_NAME = "user_credential_Prefer"
        const val USER_DATA = "user_login_data"
        const val LOCATION_LIST = "location_list"
        const val IS_FIRST_LAUNCH = "isFirstLaunch"
        const val LATEST_SEARCH = "latest_search"
        const val READ_TERMS = "read_terms"
        const val NOTIFICATION_SWITCH = "isSelected"
    }

    var isFirstLaunch: Boolean
        get() = sharedPreferences.getBoolean(IS_FIRST_LAUNCH, true)
        set(value) = sharedPreferences.edit { putBoolean(IS_FIRST_LAUNCH, value) }
    var isTermsAccept: Boolean
        get() = sharedPreferences.getBoolean(READ_TERMS, false)
        set(value) = sharedPreferences.edit { putBoolean(READ_TERMS, value) }
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    var notificationSwitchSelected: Boolean
        get() = sharedPreferences.getBoolean(NOTIFICATION_SWITCH, false)
        set(value) = sharedPreferences.edit { putBoolean(NOTIFICATION_SWITCH, value) }
    private val gson = Gson()

    fun saveUserPreference(loginData: AuthData) {
        sharedPreferences.edit { putString(USER_DATA, gson.toJson(loginData)) }
    }

    fun getUserPreference(): AuthData? {
        val json = sharedPreferences.getString(USER_DATA, null)
        return try {
            gson.fromJson(json, AuthData::class.java)
        } catch (e: Exception) {
            null
        }
    }

    var lastSearchMetal: String
        get() = sharedPreferences.getString(LATEST_SEARCH, "iron") ?: ""
        set(value) = sharedPreferences.edit { putString(LATEST_SEARCH, value) }

    fun saveLocationList(locationData: LocationResponse) {
        sharedPreferences.edit {
            putString(LOCATION_LIST, gson.toJson(locationData))
        }
    }

    fun getLocationList(): LocationResponse? {
        val json = sharedPreferences.getString(LOCATION_LIST, null)
        return try {
            gson.fromJson(json, LocationResponse::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun clearStorage() = sharedPreferences.edit().clear().apply()

}