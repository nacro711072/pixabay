package com.nacro.compose.pixabay.logic

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

fun <T> Flow<Result<T>>.catchCommonException(): Flow<Result<T>> {
    return catch { e ->
        emit(Result.failure(e))
    }
}
