package com.example.basejetpackcompose.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

open class BaseRepository {
    protected fun <T> emitData(request: suspend () -> T): Flow<T> {
        return flow { emit(request()) }.flowOn(Dispatchers.IO)
    }
}