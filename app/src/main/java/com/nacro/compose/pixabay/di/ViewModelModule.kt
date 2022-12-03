package com.nacro.compose.pixabay.di

import com.nacro.compose.pixabay.ui.page.main.MainViewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory {
        MainViewModel(get(), get(), get(), get())
    }
}