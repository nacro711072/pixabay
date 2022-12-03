package com.nacro.compose.pixabay.repository

import com.nacro.compose.pixabay.SharedPreferencesManager
import com.nacro.compose.pixabay.domain.DefaultLayout

class LayoutRepository(
    private val preference: SharedPreferencesManager
) {

    fun getDefaultLayout(): DefaultLayout {
        return preference.defaultLayout
    }
}