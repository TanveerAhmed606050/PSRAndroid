plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.android)
    kotlin("kapt")
    alias(libs.plugins.google.service.gms)
    id("kotlin-parcelize")
    alias(libs.plugins.kotlin.compose)

}

android {
    namespace = "com.example.psp_android"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.psp_android"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.window)
    implementation(libs.androidx.window)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.exoplayer)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //hilt
    implementation(libs.hilt.navigation.compose)
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.compiler)
    //navigation
    implementation(libs.androidx.navigation.common.ktx)
    implementation(libs.androidx.navigation.compose)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    // coil
    implementation(libs.coil.compose)
    //camera permission
    implementation (libs.accompanist.permissions)

    //Camerax
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation (libs.guava)
    // multidex
    implementation(libs.androidx.multidex)
    //custom toast
    implementation(libs.toast)
    //compose adaptive layout
    implementation(libs.accompanist.adaptive)
    implementation(libs.accompanist)
    //Swipe Refresh
    implementation(libs.swipe.refresh)
    //google ads
    implementation(libs.play.services.ads)
    implementation(libs.play.services.gsm)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.firebase.database)
    implementation(platform(libs.firebase.bom))
    // Pager libs
    implementation(libs.accompanist.pager)
    implementation(libs.pager.indicators)
    //paging 3 library
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime.ktx)

}