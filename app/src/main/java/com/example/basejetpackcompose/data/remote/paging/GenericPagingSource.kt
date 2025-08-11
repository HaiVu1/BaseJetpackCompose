package com.example.basejetpackcompose.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.delay

class GenericPagingSource<T : Any>(
    private val pagingApi: PagingApi<T>,
    private val limit: Int
) : PagingSource<Int, T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val offset = params.key ?: 0

        return try {
            val data = pagingApi.fetchData(offset, limit)
            delay(2000L)

            LoadResult.Page(
                data = data,
                prevKey = if (offset == 0) null else offset - limit,
                nextKey = if (data.isEmpty()) null else offset + limit
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? = 0
}