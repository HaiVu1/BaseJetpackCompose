package com.example.basejetpackcompose.domain

import com.example.basejetpackcompose.data.remote.model.PokemonDetail
import com.example.basejetpackcompose.data.repository.pokemon.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPokemonUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : UseCase<PokemonDetail, Int>() {
    override fun run(params: Int): Flow<PokemonDetail> {
        return pokemonRepository.getPokemonDetail(params)
    }
}