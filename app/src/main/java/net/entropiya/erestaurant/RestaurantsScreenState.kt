package net.entropiya.erestaurant

data class RestaurantsScreenState(
    val restaurants: List<Restaurant> = listOf(), val isLoading: Boolean = false, val error: String? = null
)
