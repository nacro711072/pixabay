package com.nacro.compose.pixabay.ui.page.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import com.nacro.compose.pixabay.ui.page.main.data.AutoCompleteQueryItem
import com.nacro.compose.pixabay.ui.theme.PixabayTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.searchImage()

        lifecycleScope.launchWhenStarted {
            viewModel.errMsg.collect {
                Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
            }
        }

        setContent {
            PixabayTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val userList by viewModel.image.collectAsState()
                    val showLoading by viewModel.showLoading.collectAsState(initial = true)
                    val queryHistory by viewModel.queryHistory.collectAsState()

                    MainScreen(
                        imageList = userList,
                        defaultDisplayType = viewModel.defaultDisplayType,
                        queryHistory = queryHistory.map { AutoCompleteQueryItem(it) },
                        onSearch = viewModel::searchImage,
                        onBottomReached = viewModel::getMore
                    )

                    if (showLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Transparent)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}
