package com.example.psrandroid.di

import android.app.Application
import android.content.Context
import com.example.psrandroid.storage.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideUserPreference(context: Context): UserPreferences {
        return UserPreferences(context)
    }

}