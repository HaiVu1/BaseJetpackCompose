package com.example.basejetpackcompose.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.basejetpackcompose.presentation.listpokemon.PokemonScreen
import com.example.basejetpackcompose.presentation.pokemondetail.PokemonDetailScreen

fun NavGraphBuilder.appNavGraph(navController: NavHostController) {
    composable(Destinations.LIST_SCREEN) {
        PokemonScreen(
            onItemClick = { id ->
                navController.navigate(Destinations.Detail.createRoute(id.toString()))
            }
        )
    }

    composable(Destinations.Detail.ROUTE) { backStackEntry ->
        val itemId = backStackEntry.arguments?.getString("itemId") ?: return@composable
        PokemonDetailScreen(pokemonId = itemId, onLeftClick = {
            navController.popBackStack()
        })
    }
}