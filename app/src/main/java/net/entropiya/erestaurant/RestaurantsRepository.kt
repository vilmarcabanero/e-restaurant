package net.entropiya.erestaurant

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.UnknownHostException

class RestaurantsRepository {
    private val restInterface = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://erestaurant-bfd3d-default-rtdb.asia-southeast1.firebasedatabase.app/").build()
        .create(RestaurantsApiService::class.java)

    private val restaurantsDao = RestaurantsDb.getDaoInstance(RestaurantsApplication.getAppContext())

    suspend fun getAllRestaurants(): List<Restaurant> {
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
            return@withContext restaurantsDao.getAll()
        }
    }

    suspend fun toggleFavoriteRestaurant(id: Int, oldValue: Boolean) = withContext(Dispatchers.IO) {
        restaurantsDao.update(
            PartialRestaurant(id = id, isFavorite = !oldValue)
        )
        restaurantsDao.getAll()
    }

    private suspend fun refreshCache() {
        val remoteRestaurants = restInterface.getRestaurants()
        val favoriteRestaurants = restaurantsDao.getAllFavorited()
        restaurantsDao.addAll(remoteRestaurants)
        restaurantsDao.updateAll(favoriteRestaurants.map {
            PartialRestaurant(it.id, true)
        })
    }

    suspend fun getRemoteRestaurant(id: Int): Restaurant {
        return withContext(Dispatchers.IO) {
            val responseMap = restInterface.getRestaurant(id)
            return@withContext responseMap.values.first()
        }
    }
}