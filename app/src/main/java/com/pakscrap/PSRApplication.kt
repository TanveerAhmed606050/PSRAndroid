package com.pakscrap

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import com.pakscrap.utils.LocaleHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PSRApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        LocaleHelper.updateLocale(this, LocaleHelper.getLanguage(this))
    }

    override fun attachBaseContext(base: Context) {
        val newBase = LocaleHelper.updateLocale(base, LocaleHelper.getLanguage(base))
        super.attachBaseContext(newBase)
    }
}