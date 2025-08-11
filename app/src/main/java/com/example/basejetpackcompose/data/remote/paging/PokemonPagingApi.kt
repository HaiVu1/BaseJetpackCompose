package com.example.basejetpackcompose.data.remote.paging

import com.example.basejetpackcompose.data.remote.model.Pokemon
import com.example.basejetpackcompose.data.remote.service.PokemonServices
import javax.inject.Inject

class PokemonPagingApi @Inject constructor(
    private val pokemonServices: PokemonServices
) : PagingApi<Pokemon> {
    override suspend fun fetchData(offset: Int, limit: Int): List<Pokemon> {
        return pokemonServices.getPokemonList(offset, limit).results ?: emptyList()
    }
}