package net.entropiya.erestaurant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RestaurantDetailsScreen() {
    val viewModel: RestaurantDetailsViewModel = viewModel()
    val state = viewModel.state.value
    state.restaurant?.let {
        Surface(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                RestaurantIcon(
                    Icons.Filled.Place, Modifier.padding(
                        top = 32.dp, bottom = 32.dp
                    )
                )
                RestaurantDetails(
                    it, Modifier.padding(bottom = 32.dp), Alignment.CenterHorizontally
                )
                Text("More info coming soon!")
            }
        }
    }
}