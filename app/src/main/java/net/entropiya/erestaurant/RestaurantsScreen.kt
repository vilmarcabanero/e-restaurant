package net.entropiya.erestaurant

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.*
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
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
fun RestaurantsScreen() {
    val viewModel: RestaurantsViewModel = viewModel()
    LazyColumn(
        modifier = Modifier.background(MaterialTheme.colors.background),
        contentPadding = PaddingValues(
            vertical = 8.dp,
        ),
    ) {
        items(viewModel.state.value) { restaurant ->
            RestaurantItem(restaurant) { id ->
                viewModel.toggleFavorite(id)
            }
        }
    }
}

@Composable
fun RestaurantItem(item: Restaurant, onClick: (id: Int) -> Unit) {
    val icon = if (item.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
    Card(
        modifier = Modifier.padding(vertical = 4.dp),
        shape = RoundedCornerShape(0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            RestaurantIcon(
                Icons.Filled.Place,
                Modifier
                    .weight(0.15f)
                    .align(alignment = Alignment.Top)
            )
            RestaurantDetails(
                item,
                Modifier.weight(0.70f)
            )
            FavoriteIcon(icon, Modifier.weight(0.15f), item.isFavorite) {
                onClick(item.id)
            }
        }
    }
}

@Composable
private fun RestaurantIcon(
    icon: ImageVector,
    modifier: Modifier
) {
    Image(
        imageVector = icon,
        contentDescription = "Restaurant icon",
        modifier = modifier
            .padding(2.5.dp),
        colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground),
    )
}

@Composable
private fun RestaurantDetails(item: Restaurant, modifier: Modifier) {
    Column(modifier = modifier) {
        Text(
            text = item.title,
            style = MaterialTheme.typography.h6
        )
        CompositionLocalProvider(
            LocalContentAlpha provides
                    ContentAlpha.medium
        ) {
            Text(
                text = item.description,
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Composable
private fun FavoriteIcon(
    icon: ImageVector,
    modifier: Modifier,
    favoriteState: Boolean,
    onClick: () -> Unit
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