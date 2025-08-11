package com.example.basejetpackcompose.base

import android.util.Log
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.basejetpackcompose.utils.NetworkHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

abstract class BaseViewModel(
    private val networkHandler: NetworkHandler
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiEvent>(UiEvent.Init)
    val uiState = _uiState.asStateFlow()

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
                    _uiState.emit(UiEvent.ShowLoading(false))
                    val httpCode = (ex as? retrofit2.HttpException)?.code() ?: -1
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
                            if (_uiState.value == UiEvent.Init) {
                                Log.d("HaiVV", "loading dialog")
                                _uiState.emit(UiEvent.ShowLoading(true))
                            }
                        }

                        is LoadState.NotLoading -> {
                            Log.d("HaiVV", "NotLoading")
                            _uiState.emit(UiEvent.ShowLoading(false))
                        }

                        is LoadState.Error -> {
                            Log.d("HaiVV", "Error")
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