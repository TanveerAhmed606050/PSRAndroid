package com.example.psrandroid.utils

import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.activity.ComponentActivity
import io.github.rupinderjeet.kprogresshud.KProgressHUD


fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

fun Context.progressBar(): KProgressHUD = KProgressHUD.create(this.getActivity())
    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
    .setCancellable(true)
    .setAnimationSpeed(1)
    .setDimAmount(0.5f)

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}
fun KProgressHUD.isVisible(isLoading: Boolean): Any = if (isLoading) this.show() else this.dismiss()
