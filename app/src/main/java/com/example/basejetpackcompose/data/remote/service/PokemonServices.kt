package com.example.basejetpackcompose.data.remote.service

import com.example.basejetpackcompose.data.remote.model.ListPokemon
import com.example.basejetpackcompose.data.remote.model.PokemonDetail
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonServices {
    @GET("api/v2//pokemon")
    suspend fun getPokemonList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): ListPokemon

    @GET("api/v2//pokemon/{id}")
    suspend fun getPokemon(@Path("id") id: Int): PokemonDetail
}