package com.nacro.compose.pixabay.ui.page.main

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.nacro.compose.pixabay.R
import com.nacro.compose.pixabay.ext.OnBottomReached
import com.nacro.compose.pixabay.ui.page.main.data.ImageItem
import com.nacro.compose.pixabay.ext.openWeb
import com.nacro.compose.pixabay.ui.component.autocomplete.ValueAutoCompleteEntity
import com.nacro.compose.pixabay.ui.page.main.data.DisplayType
import com.nacro.compose.pixabay.ui.theme.ShimmerColors
import com.nacro.compose.pixabay.ui.transformation.CircleStrokeTransformation
import com.nacro.compose.pixabay.ui.transformation.Stroke


@Composable
fun MainScreen(
    imageList: List<ImageItem> = listOf(),
    defaultDisplayType: DisplayType = DisplayType.Grid,
    queryHistory: List<ValueAutoCompleteEntity<String>> = listOf(),
    onSearch: (q: String) -> Unit = {},
    onBottomReached: () -> Unit = {},
) {


    Column(modifier = Modifier.fillMaxSize()) {
        var display by remember(defaultDisplayType) { mutableStateOf(defaultDisplayType) }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 8.dp)
                .align(Alignment.CenterHorizontally)
                .zIndex(2f)
        ) {
            SearchBarScreen(queryHistory, onSearch)
        }

        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
        ) {
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterStart),
                text = stringResource(id = R.string.search_result),
                textAlign = TextAlign.Left,
            )

            IconButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = { display = display.switch() }) {
                Icon(
                    imageVector = if (display == DisplayType.List) Icons.Filled.GridView else Icons.Filled.List,
                    contentDescription = "Clear"
                )

            }

        }

        Box(
            modifier = Modifier
                .wrapContentSize()
                .zIndex(1f)
        ) {
            when (display) {
                DisplayType.Grid -> {
                    val state = rememberLazyGridState()
                    state.OnBottomReached(onBottomReached)

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        state = state,
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        items(imageList) { item ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(4.dp))
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(item.imageUrl)
                                        .placeholder(R.drawable.pixabay_logo_square)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "image",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f),
                                    contentScale = ContentScale.Crop,
                                )
                            }
                        }
                    }
                }
                DisplayType.List -> {
                    LazyColListView(imageList, onBottomReached)

                }
            }

        }
    }


}

@Composable
private fun LazyColListView(
    userList: List<ImageItem>,
    onBottomReached: () -> Unit = {}
) {
    val context = LocalContext.current
    val lazyState = rememberLazyListState()
    lazyState.OnBottomReached(onBottomReached)

    LazyColumn(Modifier.fillMaxSize(), state = lazyState) {
        items(items = userList, key = { item ->
            item.id
        }) { item ->
            ImageItemCell(imageItem = item)
            Divider(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 2.dp),
                color = Color.LightGray
            )
        }

        items(3) {
            ShimmerItem(ShimmerColors)
        }
    }
}

@Composable
private fun ImageItemCell(imageItem: ImageItem, onClick: () -> Unit = {}) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onClick.invoke() }
            .padding(16.dp)
            .height(128.dp)
    ) {

        val density = LocalDensity.current
        AsyncImage(
            modifier = Modifier
                .size(128.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White)
                .padding(2.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageItem.imageUrl)
                .crossfade(true)
                .placeholder(R.drawable.pixabay_logo_square)
                .transformations(
                    with(density) { RoundedCornersTransformation(4.dp.toPx()) }
                )
                .build(),
            contentDescription = "image",
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight()
                .align(Alignment.CenterVertically),
            text = imageItem.name,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.body1,
        )

        Spacer(modifier = Modifier.width(16.dp))

        AsyncImage(
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.CenterVertically)
                .clip(CircleShape)
                .background(Color.White)
                .padding(2.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageItem.avatarUrl)
                .crossfade(true)
                .transformations(
                    CircleStrokeTransformation(Stroke(4f, Color.Blue.toArgb()))
                )
                .build(),
            contentDescription = "avatar"
        )


    }
}

@Composable
private fun ShimmerItem(shimmerColors: List<Color>) {

    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        )
    )

    val brush = linearGradient(
        colors = shimmerColors,
        start = Offset(10f, 10f),
        end = Offset(translateAnim, translateAnim)
    )
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(128.dp)
    ) {

        Spacer(
            modifier = Modifier
                .size(128.dp)
                .background(brush)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Spacer(
            modifier = Modifier
                .weight(1f)
                .height(24.dp)
                .align(Alignment.CenterVertically)
                .background(brush),
        )
    }

}