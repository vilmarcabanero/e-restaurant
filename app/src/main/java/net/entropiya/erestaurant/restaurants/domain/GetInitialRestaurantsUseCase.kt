package net.entropiya.erestaurant.restaurants.domain

import net.entropiya.erestaurant.restaurants.data.RestaurantsRepository

class GetInitialRestaurantsUseCase {
    private val repository = RestaurantsRepository()
    private val getSortedRestaurantsUseCase = GetSortedRestaurantsUseCase()
    suspend operator fun invoke(): List<Restaurant> {
        repository.loadRestaurants()
        return getSortedRestaurantsUseCase()
    }
}