package com.pakscrap.repository

import com.pakscrap.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseRepository {
    suspend fun <T> loadResource(query: suspend () -> T): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                Result.Success(query())
            } catch (exception: Exception) {
                Result.Failure(exception)
            }
        }
    }
}