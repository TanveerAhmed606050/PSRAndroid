package com.pakscrap.utils

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import java.util.Locale

object LocaleHelper {
//    fun updateLocale(context: Context, language: String): Context {
//        val locale = Locale(language)
//        Locale.setDefault(locale)
//
//        val configuration = context.resources.configuration
//        configuration.setLocale(locale)
//        configuration.setLayoutDirection(locale)
//
//        return context.createConfigurationContext(configuration)
//    }

    fun updateLocale(context: Context, language: String): Context {
        val locale = when {
            language == "ur" && !Locale.getAvailableLocales().contains(Locale("ur")) -> Locale("en")
            else -> Locale(language)
        }
        Locale.setDefault(locale)

        val resources = context.resources
        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }
    fun getLanguage(context: Context): String {
        val language = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
            .getString("My_Lang", "en") ?: "en"
        Log.d("LocaleHelper", "Retrieved language: $language")
        return language
    }
    fun setLanguage(context: Context, language: String) {
        val editor = context.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", language)
        editor.apply()
        Log.d("LocaleHelper", "Language set to: $language")
    }
}