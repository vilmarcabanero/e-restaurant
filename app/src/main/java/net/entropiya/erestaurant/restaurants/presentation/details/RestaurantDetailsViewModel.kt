package net.entropiya.erestaurant.restaurants.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import net.entropiya.erestaurant.restaurants.data.RestaurantsRepository
import javax.inject.Inject

@HiltViewModel
class RestaurantDetailsViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val repository: RestaurantsRepository,
) : ViewModel() {

    private var _state by mutableStateOf(RestaurantDetailsScreenState())
    val state: RestaurantDetailsScreenState
        get() = _state

    init {
        val id = stateHandle.get<Int>("restaurant_id") ?: 0
        viewModelScope.launch {
            val restaurant = repository.getRestaurant(id)
            _state = _state.copy(restaurant = restaurant)
        }
    }
}