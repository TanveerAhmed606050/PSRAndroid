package com.pakscrap.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {
    fun updateLocale(context: Context, languageName: String): Context {
        val locale = when {
            languageName == "ur" && !Locale.getAvailableLocales().contains(Locale("ur")) -> Locale("en")
            else -> Locale(languageName)
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
            .getString("saved_Lang", "en") ?: "en"
        return language
    }

    fun setLanguage(context: Context, languageName: String) {
        val editor = context.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("saved_Lang", languageName)
        editor.apply()
    }
}