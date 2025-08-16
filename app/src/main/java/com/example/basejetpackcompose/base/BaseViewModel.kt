package com.example.basejetpackcompose.base

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.basejetpackcompose.utils.NetworkHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

abstract class BaseViewModel(
    private val networkHandler: NetworkHandler
) : ViewModel() {
    private val _uiState = MutableSharedFlow<UiEvent>(replay = 1)
    val uiState = _uiState.asSharedFlow()

    init {
        viewModelScope.launch {
            _uiState.emit(UiEvent.Init)
        }
    }

    fun <T> emitDataAndHandleError(
        flowData: Flow<T>,
        stateFlow: MutableStateFlow<T>,
        needLoading: Boolean = false,
    ) {
        viewModelScope.launch {
            if (!networkHandler.isNetworkAvailable()) {
                _uiState.emit(UiEvent.NoInternet)
                return@launch
            }
            flowData
                .onStart {
                    if (needLoading) {
                        _uiState.emit(UiEvent.ShowLoading(true))
                    }
                }.catch { ex ->
                    _uiState.emit( UiEvent.ShowLoading(false))
                    val httpCode = (ex as? HttpException)?.code() ?: -1
                    _uiState.emit(UiEvent.NetworkError(httpCode))
                }.collect { data ->
                    _uiState.emit(UiEvent.ShowLoading(false))
                    stateFlow.emit(data)
                }
        }
    }

    fun <T : Any> handleStatePagingItem(
        lazyPagingItems: LazyPagingItems<T>,
    ) {
        viewModelScope.launch {
            snapshotFlow { lazyPagingItems.loadState }
                .collect { loadState ->
                    when (val refreshState = loadState.refresh) {
                        is LoadState.Loading -> {
                            if (_uiState.first() == UiEvent.Init) {
                                _uiState.emit(UiEvent.ShowLoading(true))
                            }
                        }

                        is LoadState.NotLoading -> {
                            _uiState.emit(UiEvent.ShowLoading(false))
                        }

                        is LoadState.Error -> {
                            _uiState.emit(UiEvent.ShowLoading(false))
                            val error = refreshState.error
                            if (error is IOException) {
                                _uiState.emit(UiEvent.NoInternet)
                                return@collect
                            }
                            val httpCode = (error as? HttpException)?.code() ?: -1
                            _uiState.emit(UiEvent.NetworkError(httpCode))
                        }
                    }
                }
        }
    }

    fun resetUIState() {
        viewModelScope.launch {
            _uiState.emit(UiEvent.Reset)
        }
    }
}