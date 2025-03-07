package com.example.psrandroid.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.psrandroid.network.paging.GenericPagingDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GenericPagingRepository @Inject constructor() :
    BaseRepository() {
    fun <T : Any, R> getPagingData(
        requestData: R,
        updateRequest: (R, Int) -> R,
        fetchData: suspend (R) -> List<T>
    ): Flow<PagingData<T>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            prefetchDistance = 2,
            initialLoadSize = 10
        ),
        pagingSourceFactory = {
            GenericPagingDataSource(
                apiService = fetchData,
                requestData = requestData,
                updateRequest = updateRequest
            )
        }
    ).flow
}