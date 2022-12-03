package com.nacro.compose.pixabay.ui.page.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nacro.compose.pixabay.domain.DefaultLayout
import com.nacro.compose.pixabay.repository.HistoryRepository
import com.nacro.compose.pixabay.repository.LayoutRepository
import com.nacro.compose.pixabay.repository.PixabayImageRepository
import com.nacro.compose.pixabay.ui.page.main.data.DisplayType
import com.nacro.compose.pixabay.ui.page.main.data.ImageItem
import com.nacro.compose.pixabay.domain.SearchInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class MainViewModel(
    private val imageRepository: PixabayImageRepository,
    private val historyRepository: HistoryRepository,
    private val layoutRepository: LayoutRepository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {
    companion object {
        private const val IMAGE_STATE_HANDLE_KEY = "imageList"
        private const val DEFAULT_PER_PAGE = 20
    }

    //    ImageItem(1, "test", "", "https://cdn.pixabay.com/photo/2022/11/29/11/30/dry-7624331_150.jpg")
    private val _images: MutableStateFlow<List<ImageItem>> = MutableStateFlow(
        stateHandle[IMAGE_STATE_HANDLE_KEY] ?: listOf()
    )
    val image = _images.asStateFlow()

    private val _errMsg = MutableSharedFlow<String>()
    val errMsg = _errMsg.asSharedFlow()

    private val _showLoading = MutableSharedFlow<Boolean>()
    val showLoading = _showLoading.asSharedFlow()

    private val _queryHistory = MutableStateFlow<Set<String>>(HashSet())
    val queryHistory = _queryHistory.asStateFlow()

    val defaultDisplayType: DisplayType = when(layoutRepository.getDefaultLayout()) {
        DefaultLayout.Grid -> DisplayType.Grid
        DefaultLayout.List -> DisplayType.List
    }

    init {
        val h = historyRepository.getHistory()

        viewModelScope.launch {
            _queryHistory.value = h.toSet()
            _queryHistory.collect {
                historyRepository.saveHistory(it)
            }
        }
    }

    private var currentSearchInfo = SearchInfo("", 1, DEFAULT_PER_PAGE)

    private val mutex: Mutex = Mutex()

    init {
        viewModelScope.launch {
            _images.collect {
                stateHandle[IMAGE_STATE_HANDLE_KEY] = it
            }
        }
    }

    fun searchImage(query: String = "") {

        viewModelScope.launch(Dispatchers.IO) {
            mutex.withLock {
                imageRepository.searchImage(query)
                    .onStart { _showLoading.emit(true) }
                    .onCompletion {
                        _showLoading.emit(false)
                        val set = _queryHistory.value
                        if (query.isNotBlank() && !set.contains(query)) {
                            _queryHistory.value = HashSet(set).also { it.add(query) }
                        }
                    }
                    .collect { result ->
                        result.onSuccess { newImages ->
                            _images.update { newImages }
                            currentSearchInfo = currentSearchInfo.copy(
                                query = query,
                                currentPage = 1,
                                perPage = DEFAULT_PER_PAGE
                            )
                        }.onFailure {
                            _errMsg.emit(it.message ?: "Unknown Error")
                        }
                    }
            }
        }
    }

    fun getMore() {
        if (mutex.isLocked) return

        viewModelScope.launch(Dispatchers.IO) {
            mutex.withLock {
                imageRepository.searchImage(
                    currentSearchInfo.query,
                    currentSearchInfo.currentPage + 1,
                    currentSearchInfo.perPage
                )
                    .onStart { _showLoading.emit(true) }
                    .onCompletion { _showLoading.emit(false) }
                    .collect { result ->
                        result.onSuccess { newList ->
                            _images.getAndUpdate { oldList ->
                                oldList + newList
                            }
                            val old = currentSearchInfo
                            currentSearchInfo =
                                currentSearchInfo.copy(currentPage = old.currentPage + 1)
                        }.onFailure {
                            _errMsg.emit(it.message ?: "Unknown Error")
                        }
                    }
            }
        }
    }
}