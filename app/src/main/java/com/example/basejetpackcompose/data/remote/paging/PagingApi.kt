package com.example.basejetpackcompose.data.remote.paging

interface PagingApi<T : Any> {
    suspend fun fetchData(offset: Int, limit: Int): List<T>
}