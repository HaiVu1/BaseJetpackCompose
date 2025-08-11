package com.example.basejetpackcompose.domain

import kotlinx.coroutines.flow.Flow

abstract class UseCase<Type, in Params> where Type : Any? {

    protected abstract fun run(params: Params): Flow<Type>

    operator fun invoke(params: Params): Flow<Type> {
        return run(params)
    }

    class None
}