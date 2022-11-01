package net.entropiya.erestaurant

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RestaurantDetailsViewModel(stateHandle: SavedStateHandle) : ViewModel() {
    private val repository = RestaurantsRepository()
    private val restaurantsDao = RestaurantsDb.getDaoInstance(RestaurantsApplication.getAppContext())
    private val _state = mutableStateOf(RestaurantDetailsScreenState())
    val state: State<RestaurantDetailsScreenState>
        get() = _state

    init {
        val id = stateHandle.get<Int>("restaurant_id") ?: 0
        viewModelScope.launch {
            val restaurant = restaurantsDao.findOne(id) ?: repository.getRemoteRestaurant(id)
            _state.value = _state.value.copy(restaurant = restaurant)
        }
    }
}