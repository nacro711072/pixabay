package com.nacro.compose.pixabay.ui.page.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nacro.compose.pixabay.ui.component.autocomplete.AutoCompleteBox
import com.nacro.compose.pixabay.ui.component.autocomplete.ValueAutoCompleteEntity

@Composable
fun SearchBarScreen(
    queryHistory: List<ValueAutoCompleteEntity<String>> = listOf(),
    onSearch: (q: String) -> Unit = {}
) {
    AutoCompleteBox(
        modifier = Modifier
            .wrapContentHeight()
            .imePadding(),
        items = queryHistory,
        itemContent = { item ->
            Text(
                modifier = Modifier.padding(16.dp),
                text = item.value,
                style = MaterialTheme.typography.body1
            )
        }
    ) {
        var value by remember { mutableStateOf("") }
        val view = LocalView.current

        onItemSelected { item ->
            value = item.value
            filter(value)
            view.clearFocus()
            onSearch(value)
        }

        TextSearchBar(
            modifier = Modifier,
            value = value,
            label = "Search image",
            onDoneActionClick = {
                view.clearFocus()
                onSearch(value)
            },
            onClearClick = {
                value = ""
                filter(value)
                view.clearFocus()
            },
            onFocusChanged = { focusState ->
                isSearching = focusState.isFocused
                filter(value)
            },
            onValueChanged = { query ->
                value = query
                filter(value)
            }
        )
    }
//    Box(modifier = Modifier.wrapContentSize()) {
//        TextSearchBar(value = searchValue, label = "input keyword to start search. ex: cat", onValueChanged = onSearchValueChanged)
//    }
}

@Composable
fun TextSearchBar(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onFocusChanged: (FocusState) -> Unit = {},
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { onFocusChanged(it) },
        value = value,
        onValueChange = { query ->
            onValueChanged(query)
        },
        label = { Text(text = label) },
        textStyle = MaterialTheme.typography.subtitle1,
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = { onClearClick() }) {
                Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
            }
        },
        keyboardActions = KeyboardActions(onDone = { onDoneActionClick() }),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        )
    )
}

class TestName(override val value: String) : ValueAutoCompleteEntity<String> {
    override fun filter(query: String): Boolean {
        return value.contains(query, ignoreCase = true)
    }
}