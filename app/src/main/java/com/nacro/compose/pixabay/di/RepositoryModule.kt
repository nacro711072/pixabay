package com.nacro.compose.pixabay.di

import com.nacro.compose.pixabay.repository.HistoryRepository
import com.nacro.compose.pixabay.repository.LayoutRepository
import com.nacro.compose.pixabay.repository.PixabayImageRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory {
        PixabayImageRepository(get(), get())
    }

    factory {
        HistoryRepository(get())
    }

    factory {
        LayoutRepository(get())
    }
}