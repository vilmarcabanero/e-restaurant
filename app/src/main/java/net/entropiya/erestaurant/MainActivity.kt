package net.entropiya.erestaurant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import net.entropiya.erestaurant.ui.theme.ERestaurantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ERestaurantTheme {
                RestaurantsScreen()
            }
        }
    }
}