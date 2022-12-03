/*
 * Copyright 2021 Paulo Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nacro.compose.pixabay.ui.component.autocomplete

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


const val AutoCompleteBoxTag = "AutoCompleteBox"

@Composable
fun <T : AutoCompleteEntity> AutoCompleteBox(
    modifier: Modifier = Modifier,
    items: List<T>,
    itemContent: @Composable (T) -> Unit,
    content: @Composable AutoCompleteScope<T>.() -> Unit
) {
    val autoCompleteState = remember(items) { AutoCompleteState(startItems = items) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val view = LocalView.current
        autoCompleteState.content()
        BackHandler(autoCompleteState.isSearching) {
            view.clearFocus()
        }

        AnimatedVisibility(visible = autoCompleteState.isSearching) {
            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(autoCompleteState.filteredItems) { item ->
                    Column(modifier = Modifier.clickable { autoCompleteState.selectItem(item) }) {
                        itemContent(item)
                        Divider(modifier.padding(horizontal = 4.dp), color = Color.Gray)
                    }
                }
            }
        }
    }
}

private fun Modifier.autoComplete(
    autoCompleteItemScope: AutoCompleteDesignScope
): Modifier = composed {
    val baseModifier = if (autoCompleteItemScope.shouldWrapContentHeight)
        wrapContentHeight()
    else
        heightIn(0.dp, autoCompleteItemScope.boxMaxHeight)

    baseModifier
        .testTag(AutoCompleteBoxTag)
        .fillMaxWidth(autoCompleteItemScope.boxWidthPercentage)
}

@Stable
interface AutoCompleteEntity {
    fun filter(query: String): Boolean
}

@Stable
interface ValueAutoCompleteEntity<T> : AutoCompleteEntity {
    val value: T
}

private typealias ItemSelected<T> = (T) -> Unit

@Stable
interface AutoCompleteScope<T : AutoCompleteEntity> : AutoCompleteDesignScope {
    var isSearching: Boolean
    fun filter(query: String)
    fun onItemSelected(block: ItemSelected<T> = {})
}

@Stable
interface AutoCompleteDesignScope {
    var boxWidthPercentage: Float
    var shouldWrapContentHeight: Boolean
    var boxMaxHeight: Dp
    var boxBorderStroke: BorderStroke
    var boxShape: Shape
}

class AutoCompleteState<T : AutoCompleteEntity>(private val startItems: List<T>) : AutoCompleteScope<T> {
    private var onItemSelectedBlock: ItemSelected<T>? = null

    fun selectItem(item: T) {
        onItemSelectedBlock?.invoke(item)
    }

    var filteredItems by mutableStateOf(startItems)
    override var isSearching by mutableStateOf(false)
    override var boxWidthPercentage by mutableStateOf(.9f)
    override var shouldWrapContentHeight by mutableStateOf(false)
    override var boxMaxHeight: Dp by mutableStateOf(TextFieldDefaults.MinHeight * 3)
    override var boxBorderStroke by mutableStateOf(BorderStroke(2.dp, Color.Black))
    override var boxShape: Shape by mutableStateOf(RoundedCornerShape(8.dp))

    override fun filter(query: String) {
        filteredItems = startItems.filter { entity ->
            entity.filter(query)
        }
    }

    override fun onItemSelected(block: ItemSelected<T>) {
        onItemSelectedBlock = block
    }
}