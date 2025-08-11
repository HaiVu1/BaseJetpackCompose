package com.example.basejetpackcompose.data.repository.di

import com.example.basejetpackcompose.data.repository.pokemon.PokemonRepository
import com.example.basejetpackcompose.data.repository.pokemon.PokemonRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun providePokemonRepository(pokemonRepositoryImpl: PokemonRepositoryImpl): PokemonRepository
}