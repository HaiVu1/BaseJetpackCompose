package com.example.basejetpackcompose.presentation.listpokemon

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.basejetpackcompose.base.BaseViewModel
import com.example.basejetpackcompose.data.remote.model.Pokemon
import com.example.basejetpackcompose.domain.GetListPokemonUseCase
import com.example.basejetpackcompose.utils.NetworkHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val getListPokemonUseCase: GetListPokemonUseCase,
    private val networkHandler: NetworkHandler,
) : BaseViewModel(networkHandler) {
    val pokemonPagingFlow: Flow<PagingData<Pokemon>> =
        getListPokemonUseCase(Any()).cachedIn(viewModelScope)
}