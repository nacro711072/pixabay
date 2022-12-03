package com.nacro.compose.pixabay.domain

data class SearchInfo(
    val query: String,
    val currentPage: Int,
    val perPage: Int
)