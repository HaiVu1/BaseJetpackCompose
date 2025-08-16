package com.example.basejetpackcompose.base

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> BaseHandleStateScreen(
    uiState: State<T?>,
    uiEventFlow: Flow<UiEvent>,
    onForceLogout: () -> Unit,
    title: String,
    titleAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    leftIcon: ImageVector? = null,
    onLeftClick: (() -> Unit)? = null,
    rightContent: @Composable (() -> Unit)? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    modifier: Modifier,
    content: @Composable (T) -> Unit,
) {
    var showLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showForceLogoutDialog by remember { mutableStateOf(false) }
    var noInternet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        uiEventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowLoading -> {
                    showLoading = event.isShow
                }

                is UiEvent.NetworkError -> {
                    errorMessage = "Error call API!"
                }

                is UiEvent.ForceLogout -> showForceLogoutDialog = true
                is UiEvent.NoInternet -> noInternet = true
                UiEvent.Init -> {}
                UiEvent.Reset -> {}
            }
        }
    }
    Column(modifier = modifier) {
        CustomAppbarLayout(
            title = title,
            titleAlignment = titleAlignment,
            leftIcon = leftIcon,
            onLeftClick = onLeftClick,
            rightContent = rightContent,
            backgroundColor = backgroundColor,
            contentColor = contentColor
        )
        uiState.value?.apply {
            content(this)
        }
    }

    if (showLoading) {
        LoadingDialog()
    }

    ErrorDialog(errorMessage = errorMessage, onDismissRequest = { errorMessage = null }) {
        errorMessage = null
    }

    // Show no internet dialog
    if (noInternet) {
        NoInternetDialog(onDismissRequest = {
            noInternet = false
        }) {
            noInternet = false
        }
    }

    // Show force logout dialog
    if (showForceLogoutDialog) {
        ForceLogoutDialog(onDismissRequest = {
            showForceLogoutDialog = false
        }) {
            showForceLogoutDialog = false
            onForceLogout()
        }
    }
}

