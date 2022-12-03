package com.nacro.compose.pixabay.repository

import com.nacro.compose.pixabay.SharedPreferencesManager
import kotlinx.coroutines.flow.*

class HistoryRepository(
    private val preference: SharedPreferencesManager,
) {

    fun getHistory(): Set<String> {
        return preference.queryHistory
    }

    fun saveHistory(q: Set<String>) {
        preference.queryHistory = q
    }
}