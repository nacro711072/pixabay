package com.nacro.compose.pixabay.repository

import com.nacro.compose.pixabay.api.PixabayApiService
import com.nacro.compose.pixabay.logic.catchCommonException
import com.nacro.compose.pixabay.mapper.DefaultDataMapper
import com.nacro.compose.pixabay.ui.page.main.data.ImageItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PixabayImageRepository(
    private val service: PixabayApiService,
    private val mapper: DefaultDataMapper
) {
    fun searchImage(q: String, page: Int = 1, perPage: Int = 20): Flow<Result<List<ImageItem>>> {
        return flow {
            val response = service.searchImage(q = q, page = page, perPage = perPage)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val out = body.hits.map {
                        mapper.pixabayRemoteDataToImageItem(it)
                    }
                    emit(Result.success(out))
                } else {
                    throw Exception("Oops! The body is empty.")
                }
            }
        }.catchCommonException()
    }
}