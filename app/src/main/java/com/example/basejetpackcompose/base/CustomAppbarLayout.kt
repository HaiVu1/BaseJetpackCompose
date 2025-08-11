package com.example.basejetpackcompose.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun CustomAppbarLayout(
    title: String,
    titleAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    leftIcon: ImageVector? = null,
    onLeftClick: (() -> Unit)? = null,
    rightContent: @Composable (() -> Unit)? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
    Surface(
        color = backgroundColor,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leftIcon != null && onLeftClick != null) {
                IconButton(onClick = onLeftClick) {
                    Icon(imageVector = leftIcon, contentDescription = "", tint = contentColor)
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = when (titleAlignment) {
                    Alignment.Start -> Arrangement.Start
                    Alignment.CenterHorizontally -> Arrangement.Center
                    Alignment.End -> Arrangement.End

                    else -> Arrangement.Center
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = contentColor,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            rightContent?.apply {
                rightContent()
            } ?: Spacer(modifier = Modifier.width(48.dp))

        }
    }
}