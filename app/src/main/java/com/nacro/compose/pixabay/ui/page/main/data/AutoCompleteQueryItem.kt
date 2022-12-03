package com.nacro.compose.pixabay.ui.page.main.data

import com.nacro.compose.pixabay.ui.component.autocomplete.ValueAutoCompleteEntity

class AutoCompleteQueryItem(override val value: String) : ValueAutoCompleteEntity<String> {
    override fun filter(query: String): Boolean {
        return value.contains(query)
    }
}