package com.example.basejetpackcompose.presentation.pokemondetail

import com.example.basejetpackcompose.base.BaseViewModel
import com.example.basejetpackcompose.data.remote.model.PokemonDetail
import com.example.basejetpackcompose.domain.GetPokemonUseCase
import com.example.basejetpackcompose.utils.NetworkHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val networkHandler: NetworkHandler,
    private val getPokemonUseCase: GetPokemonUseCase,
) : BaseViewModel(networkHandler) {
    private val _flowPokemonDetail: MutableStateFlow<PokemonDetail?> = MutableStateFlow(null)
    val flowPokemonDetail: StateFlow<PokemonDetail?> = _flowPokemonDetail.asStateFlow()

    fun getPokemonDetail(id: Int) {
        emitDataAndHandleError(
            flowData = getPokemonUseCase.invoke(id),
            _flowPokemonDetail,
            needLoading = true
        )
    }
}