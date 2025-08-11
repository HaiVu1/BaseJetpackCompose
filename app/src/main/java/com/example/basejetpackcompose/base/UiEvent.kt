package com.example.basejetpackcompose.base

sealed class UiEvent {
    data object Init : UiEvent()
    data class ShowLoading(val isShow: Boolean) : UiEvent()
    data object NoInternet : UiEvent()
    data class NetworkError(val errorCode: Int) : UiEvent()
    data object ForceLogout : UiEvent()
    data object Reset : UiEvent()
}