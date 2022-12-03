package com.nacro.compose.pixabay.domain

sealed class DefaultLayout(val name: String) {
    object Grid: DefaultLayout("grid")
    object List: DefaultLayout("list")

    companion object {
        fun get(name: String): DefaultLayout {
            return when (name) {
                Grid.name -> Grid
                List.name -> List
                else -> Grid
            }
        }
    }
}