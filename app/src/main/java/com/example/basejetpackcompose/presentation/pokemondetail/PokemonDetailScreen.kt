package com.example.basejetpackcompose.presentation.pokemondetail

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.basejetpackcompose.base.BaseHandleStateScreen

@Composable
fun PokemonDetailScreen(
    pokemonId: String,
    onLeftClick: () -> Unit,
    viewmodel: PokemonDetailViewModel = hiltViewModel(),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    val pokemon = viewmodel.flowPokemonDetail.collectAsStateWithLifecycle()

    LaunchedEffect(pokemonId) {
        viewmodel.getPokemonDetail(id = pokemonId.toInt())
    }
    BaseHandleStateScreen(
        uiState = pokemon,
        uiEventFlow = viewmodel.uiState,
        onForceLogout = {},
        title = "Pokemon Detail",
        leftIcon = Icons.Default.ArrowBack,
        titleAlignment = Alignment.Start,
        onLeftClick = onLeftClick,
        modifier = modifier,
    ) {
        Text(text = it.name ?: "")
        Spacer(Modifier.height(10.dp))

        Text(text = it.order.toString())
        Spacer(Modifier.height(10.dp))

        Text(text = it.weight.toString())
        Spacer(Modifier.height(10.dp))
    }
}