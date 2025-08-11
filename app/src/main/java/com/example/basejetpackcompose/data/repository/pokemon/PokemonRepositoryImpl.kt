package com.example.basejetpackcompose.data.repository.pokemon

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.basejetpackcompose.data.remote.model.Pokemon
import com.example.basejetpackcompose.data.remote.model.PokemonDetail
import com.example.basejetpackcompose.data.remote.paging.GenericPagingSource
import com.example.basejetpackcompose.data.remote.paging.PokemonPagingApi
import com.example.basejetpackcompose.data.remote.service.PokemonServices
import com.example.basejetpackcompose.data.repository.BaseRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val pokemonPagingApi: PokemonPagingApi,
    private val pokemonServices: PokemonServices,
) : BaseRepository(), PokemonRepository {
    override fun getListPokemon(): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { GenericPagingSource(pokemonPagingApi, 20) }
        ).flow
    }

    override fun getPokemonDetail(id: Int): Flow<PokemonDetail> {
        return flow {
            delay(2000)
            emit(pokemonServices.getPokemon(id))
        }
    }
}