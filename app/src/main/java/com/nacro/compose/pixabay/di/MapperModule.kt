package com.nacro.compose.pixabay.di

import com.nacro.compose.pixabay.mapper.DefaultDataMapper
import org.koin.dsl.module

val mapperModule = module {
    single {
        DefaultDataMapper()
    }
}