package com.pakscrap.utils

import android.content.Context
import java.util.Locale

object LocaleHelper {
    fun updateLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }

    fun getLanguage(context: Context): String {
        return context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
            .getString("My_Lang", "en") ?: "en"
    }

    fun setLanguage(context: Context, language: String) {
        val editor = context.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", language)
        editor.apply()
    }
}