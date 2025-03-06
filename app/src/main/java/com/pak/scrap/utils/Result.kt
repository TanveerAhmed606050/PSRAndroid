package com.pak.scrap.utils

import java.lang.Exception

sealed class Result<out T> {
    data class Success<out R>(val data:R) : Result<R>()
    data class Failure(val exception: Exception) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}