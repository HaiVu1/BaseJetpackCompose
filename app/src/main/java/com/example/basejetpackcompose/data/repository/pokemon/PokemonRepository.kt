package com.example.basejetpackcompose.data.repository.pokemon

import androidx.paging.PagingData
import com.example.basejetpackcompose.data.remote.model.ListPokemon
import com.example.basejetpackcompose.data.remote.model.Pokemon
import com.example.basejetpackcompose.data.remote.model.PokemonDetail
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getListPokemon() : Flow<PagingData<Pokemon>>

    fun getPokemonDetail(id: Int): Flow<PokemonDetail>
}