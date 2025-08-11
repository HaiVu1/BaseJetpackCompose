package com.example.basejetpackcompose.presentation.navigation

object Destinations {
    const val LIST_SCREEN = "list"
    const val DETAIL_SCREEN = "detail"

    object Detail {
        const val ROUTE = "$DETAIL_SCREEN/{itemId}"
        fun createRoute(itemId: String) = "$DETAIL_SCREEN/$itemId"
    }
}