package net.entropiya.erestaurant.restaurants.presentation.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import net.entropiya.erestaurant.restaurants.domain.GetInitialRestaurantsUseCase
import net.entropiya.erestaurant.restaurants.domain.ToggleRestaurantUseCase
import javax.inject.Inject

@HiltViewModel
class RestaurantsViewModel @Inject constructor(
    private val getRestaurantsUseCase: GetInitialRestaurantsUseCase, private val toggleRestaurantsUseCase: ToggleRestaurantUseCase
) : ViewModel() {
    private var _state by mutableStateOf(
        RestaurantsScreenState()
    )
    val state: RestaurantsScreenState
        get() = _state
    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
        _state = _state.copy(
            error = exception.message, isLoading = false
        )
    }

    init {
        getRestaurants() //        _state = _state.copy( error = "Latus Rectum")
    }

    fun toggleFavorite(id: Int, oldValue: Boolean) {
        viewModelScope.launch(errorHandler) {
            val updatedRestaurants = toggleRestaurantsUseCase(id, oldValue)
            _state = _state.copy(
                restaurants = updatedRestaurants
            )
        }
    }

    private fun getRestaurants() {
        viewModelScope.launch(errorHandler) { //            _state = _state.copy(isLoading = true)
            val restaurants = getRestaurantsUseCase()
            _state = _state.copy(restaurants = restaurants)
        }
    }
}