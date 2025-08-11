package com.example.basejetpackcompose.domain

import androidx.paging.PagingData
import com.example.basejetpackcompose.data.remote.model.Pokemon
import com.example.basejetpackcompose.data.repository.pokemon.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetListPokemonUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository,
) : UseCase<PagingData<Pokemon>, Any>() {
    override fun run(params: Any): Flow<PagingData<Pokemon>> {
        return pokemonRepository.getListPokemon()
    }
}
