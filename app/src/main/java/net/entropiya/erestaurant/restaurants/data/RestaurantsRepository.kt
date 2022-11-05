package net.entropiya.erestaurant.restaurants.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.entropiya.erestaurant.restaurants.domain.Restaurant
import net.entropiya.erestaurant.RestaurantsApplication
import net.entropiya.erestaurant.restaurants.data.local.LocalRestaurant
import net.entropiya.erestaurant.restaurants.data.local.PartialLocalRestaurant
import net.entropiya.erestaurant.restaurants.data.local.RestaurantsDao
import net.entropiya.erestaurant.restaurants.data.local.RestaurantsDb
import net.entropiya.erestaurant.restaurants.data.remote.RestaurantsApiService
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

class RestaurantsRepository @Inject constructor(
    private val restInterface: RestaurantsApiService, private val restaurantsDao: RestaurantsDao
) {

    suspend fun loadRestaurants() {
        return withContext(Dispatchers.IO) {
            try {
                refreshCache()
            } catch (e: Exception) {
                when (e) {
                    is UnknownHostException, is ConnectException, is HttpException -> {
                        if (restaurantsDao.getAll().isEmpty()) throw Exception(
                            "Something went wrong. " + "We have no data."
                        )
                    }
                    else -> throw e
                }
            }
        }
    }

    suspend fun toggleFavoriteRestaurant(id: Int, value: Boolean) = withContext(Dispatchers.IO) {
        restaurantsDao.update(
            PartialLocalRestaurant(id = id, isFavorite = value)
        )
    }

    private suspend fun refreshCache() {
        val remoteRestaurants = restInterface.getRestaurants()
        val favoriteRestaurants = restaurantsDao.getAllFavorited()
        restaurantsDao.addAll(remoteRestaurants.map {
            LocalRestaurant(it.id, it.title, it.description, false)
        })
        restaurantsDao.updateAll(favoriteRestaurants.map {
            PartialLocalRestaurant(it.id, true)
        })
    }

    private suspend fun getRemoteRestaurant(id: Int): Restaurant {
        return withContext(Dispatchers.IO) {
            val responseMap = restInterface.getRestaurant(id)
            return@withContext responseMap.values.first().let {
                Restaurant(
                    id = it.id, title = it.title, description = it.description
                )
            }
        }
    }

    suspend fun getRestaurants(): List<Restaurant> {
        return withContext(Dispatchers.IO) {
            return@withContext restaurantsDao.getAll().map {
                Restaurant(
                    it.id, it.title, it.description, it.isFavorite
                )
            }
        }
    }

    suspend fun getRestaurant(id: Int): Restaurant {
        return withContext(Dispatchers.IO) {
            return@withContext restaurantsDao.findOne(id)?.let {
                Restaurant(
                    id = it.id, title = it.title, description = it.description
                )
            } ?: getRemoteRestaurant(id)
        }
    }
}
