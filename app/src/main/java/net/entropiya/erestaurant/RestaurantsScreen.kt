package net.entropiya.erestaurant

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import net.entropiya.erestaurant.ui.theme.ERestaurantTheme

@Composable
fun RestaurantsScreen(onItemClick: (id: Int) -> Unit = { }) {
    val viewModel: RestaurantsViewModel = viewModel()
    val state = viewModel.state.value
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.background(MaterialTheme.colors.background),
        ) {
            items(state.restaurants) { restaurant ->
                RestaurantItem(restaurant, onFavoriteClick = { id, oldValue ->
                    viewModel.toggleFavorite(
                        id, oldValue
                    )
                }, onItemClick = { id -> onItemClick(id) })
            }
        }
        if (state.isLoading) CircularProgressIndicator()
        state.error?.let { Text(state.error) }
    }

}

@Composable
fun RestaurantItem(
    item: Restaurant, onFavoriteClick: (id: Int, oldValue: Boolean) -> Unit, onItemClick: (id: Int) -> Unit
) {
    val icon = if (item.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
    Card(
        modifier = Modifier.clickable {
            onItemClick(item.id)
        }, shape = RoundedCornerShape(0.dp)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)
        ) {
            RestaurantIcon(
                Icons.Filled.Place, Modifier.weight(0.15f)
            )
            RestaurantDetails(
                item, Modifier.weight(0.70f)
            )
            FavoriteIcon(icon, Modifier.weight(0.15f), item.isFavorite) {
                onFavoriteClick(item.id, item.isFavorite)
            }
        }
    }
}

@Composable
fun RestaurantIcon(
    icon: ImageVector, modifier: Modifier
) {
    Image(
        imageVector = icon,
        contentDescription = "Restaurant icon",
        modifier = modifier.padding(2.5.dp),
        colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground),
    )
}

@Composable
fun RestaurantDetails(
    item: Restaurant, modifier: Modifier, horizontalAlignment: Alignment.Horizontal = Alignment.Start
) {
    Column(modifier = modifier, horizontalAlignment = horizontalAlignment) {
        Text(
            text = item.title, style = MaterialTheme.typography.h6
        )
        CompositionLocalProvider(
            LocalContentAlpha provides ContentAlpha.medium
        ) {
            Text(
                text = item.description, style = MaterialTheme.typography.body2
            )
        }
    }
}

@Composable
private fun FavoriteIcon(
    icon: ImageVector, modifier: Modifier, favoriteState: Boolean, onClick: () -> Unit
) {
    IconButton(onClick = { onClick() }) {
        Image(
            imageVector = icon,
            contentDescription = "Favorite restaurant icon",
            modifier = modifier.padding(8.dp),
            colorFilter = if (!favoriteState) ColorFilter.tint(MaterialTheme.colors.onBackground)
            else ColorFilter.tint(Color.Red)

        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ERestaurantTheme {
        RestaurantsScreen()
    }
}