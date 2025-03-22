package com.pakscrap

import android.app.Application
import android.content.Context
import android.util.Log
import com.pakscrap.utils.LocaleHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PSRApplication : Application() {
    override fun attachBaseContext(base: Context) {
        Log.d("lsjadf", "attachBaseContext: ${LocaleHelper.getLanguage(base)}")
        val newBase = LocaleHelper.updateLocale(base, LocaleHelper.getLanguage(base))
        super.attachBaseContext(newBase)
    }
}