package com.nacro.compose.pixabay.mapper

import com.nacro.compose.pixabay.api.model.PixabayRemoteData
import com.nacro.compose.pixabay.ui.page.main.data.ImageItem

class DefaultDataMapper {

    fun pixabayRemoteDataToImageItem(origin: PixabayRemoteData.Hit): ImageItem {
        return ImageItem(origin.id, origin.user, origin.userImageURL, origin.previewURL)
    }
}