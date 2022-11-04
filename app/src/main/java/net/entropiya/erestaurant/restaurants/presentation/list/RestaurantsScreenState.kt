package net.entropiya.erestaurant.restaurants.presentation.list

import net.entropiya.erestaurant.restaurants.domain.Restaurant

data class RestaurantsScreenState(
    val restaurants: List<Restaurant> = listOf(), val isLoading: Boolean = false, val error: String? = null
)
