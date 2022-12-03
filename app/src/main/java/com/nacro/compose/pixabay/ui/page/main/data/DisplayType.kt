package com.nacro.compose.pixabay.ui.page.main.data

enum class DisplayType {
    Grid, List;

    fun switch(): DisplayType {
        return when(this) {
            Grid -> List
            List -> Grid
        }
    }
}