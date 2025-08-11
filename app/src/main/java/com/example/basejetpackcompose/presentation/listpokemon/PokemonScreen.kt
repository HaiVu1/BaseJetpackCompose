package com.example.basejetpackcompose.presentation.listpokemon

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.basejetpackcompose.base.PagingListScreen
import com.example.basejetpackcompose.data.remote.model.Pokemon

@Composable
fun PokemonScreen(
    viewModel: PokemonViewModel = hiltViewModel(),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    onItemClick: (Int) -> Unit
) {
    val lazyPagingItems = viewModel.pokemonPagingFlow.collectAsLazyPagingItems()
    LaunchedEffect(Unit) {
        viewModel.handleStatePagingItem(lazyPagingItems)
    }

    PagingListScreen(
        items = lazyPagingItems,
        uiEventFlow = viewModel.uiState,
        resetUIState = { viewModel.resetUIState() },
        title = "Home",
        onForceLogout = {},
        itemContent = {
            ListItemView(it, onItemClick)
        },
        onEmptyContent = {},
        modifier = modifier,
    )

}

@Composable
fun ListItemView(
    item: Pokemon,
    onItemClick: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val id = item.url
                    ?.substringAfterLast("pokemon/")
                    ?.trimEnd('/')
                onItemClick.invoke(id?.toInt() ?: 1)
            }
            .padding(8.dp)
    ) {
        Text(
            text = item.name ?: "",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = item.url ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}
