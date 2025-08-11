package com.example.basejetpackcompose.base

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T : Any> PagingListScreen(
    items: LazyPagingItems<T>,
    uiEventFlow: Flow<UiEvent>,
    onForceLogout: () -> Unit,
    resetUIState: () -> Unit,
    title: String,
    titleAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    leftIcon: ImageVector? = null,
    onLeftClick: (() -> Unit)? = null,
    rightContent: @Composable (() -> Unit)? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    onEmptyContent: @Composable () -> Unit,
    itemContent: @Composable (T) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    var showLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showForceLogoutDialog by remember { mutableStateOf(false) }
    var noInternet by remember { mutableStateOf(false) }

    val isRefreshing = items.loadState.refresh is LoadState.Loading
    val isAppending = items.loadState.append is LoadState.Loading
    val isEmpty = items.itemCount == 0 && items.loadState.refresh is LoadState.NotLoading
    var isRefreshingError by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = (isRefreshing || isRefreshingError) && !showLoading,
        onRefresh = {
            isRefreshingError = true
            items.refresh()
        }
    )

    fun resetLoading() {
        isRefreshingError = false
        showLoading = false
    }

    LaunchedEffect(items) {
        uiEventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowLoading -> {
                    showLoading = event.isShow
                }

                is UiEvent.NetworkError -> {
                    resetLoading()
                    errorMessage = "Error call API!"
                }

                is UiEvent.ForceLogout -> {
                    resetLoading()
                    showForceLogoutDialog = true
                }

                is UiEvent.NoInternet -> {
                    resetLoading()
                    noInternet = true
                }

                UiEvent.Init -> {}
                UiEvent.Reset -> {}
            }
        }
    }

    if (showLoading) {
        LoadingDialog()
    }

    errorMessage?.let {
        BaseDialog(title = "Lỗi",
            description = it,
            onDismissRequest = {
                errorMessage = null
            }) {
            errorMessage = null
        }
    }

    if (noInternet) {
        BaseDialog(
            title = "Không có kết nối mạng",
            description = "Vui lòng kiểm tra lại kết nối Internet.",
            onDismissRequest = {
                noInternet = false
                showLoading = false
                resetUIState()
            },
        ) {
            noInternet = false
            resetUIState()
        }
    }

    if (showForceLogoutDialog) {
        BaseDialog(
            title = "Phiên đăng nhập đã hết hạn",
            textOk = "Đăng xuất",
            onDismissRequest = {
                showForceLogoutDialog = false
            }
        ) {
            showForceLogoutDialog = false
            onForceLogout()
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            when {
                isEmpty -> {
                    resetLoading()
                    onEmptyContent()
                }

                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(items.itemCount) { index ->
                            items[index]?.let { itemContent(it) }
                        }
                        if (isAppending) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = (isRefreshing || isRefreshingError) && !showLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }

}

