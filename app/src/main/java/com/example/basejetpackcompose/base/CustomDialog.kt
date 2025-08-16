package com.example.basejetpackcompose.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun CustomDialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = Color.White
        ) {
            content()
        }
    }
}

@Composable
fun BaseDialog(
    title: String,
    description: String? = null,
    textOk: String? = null,
    onCancelRequest: (() -> Unit)? = null,
    onDismissRequest: (() -> Unit),
    onClickedOk: () -> Unit
) {
    CustomDialog(onDismissRequest = onDismissRequest) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(16.dp))
            description?.apply {
                Text(this)
                Spacer(Modifier.height(24.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                onCancelRequest?.let { cancel ->
                    OutlinedButton(
                        onClick = cancel,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }
                }

                Button(
                    onClick = onClickedOk,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(textOk ?: "OK")
                }
            }
        }
    }
}

@Composable
fun LoadingDialog() {
    CustomDialog(onDismissRequest = {}) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun ErrorDialog(
    errorMessage: String? = null,
    onDismissRequest: () -> Unit,
    onClickedOk: () -> Unit,
) {
    errorMessage?.let {
        BaseDialog(
            title = "Lỗi",
            description = it,
            onDismissRequest = onDismissRequest,
            onClickedOk = onClickedOk
        )
    }
}

@Composable
fun NoInternetDialog(
    onDismissRequest: () -> Unit,
    onClickedOk: () -> Unit,
) {
    BaseDialog(
        title = "Không có kết nối mạng",
        description = "Vui lòng kiểm tra lại kết nối Internet.",
        onDismissRequest = onDismissRequest,
        onClickedOk = onClickedOk
    )
}

@Composable
fun ForceLogoutDialog(
    onDismissRequest: () -> Unit,
    onClickedOk: () -> Unit,
) {
    BaseDialog(
        title = "Phiên đăng nhập đã hết hạn",
        textOk = "Đăng xuất",
        onDismissRequest = onDismissRequest,
        onClickedOk = onClickedOk,
    )
}