package net.entropiya.erestaurant.restaurants.domain

import net.entropiya.erestaurant.restaurants.data.RestaurantsRepository
import javax.inject.Inject

class GetInitialRestaurantsUseCase @Inject constructor(
    private val repository: RestaurantsRepository, private val getSortedRestaurantsUseCase: GetSortedRestaurantsUseCase
) {
    suspend operator fun invoke(): List<Restaurant> {
        if (repository.getRestaurants().isEmpty()) repository.loadRestaurants()
        return getSortedRestaurantsUseCase()
    }
}