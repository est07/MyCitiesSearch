package com.example.mycitiessearch.presentation.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.mycitiessearch.R
import com.example.mycitiessearch.domain.models.CityModel
import com.example.mycitiessearch.presentation.ui.theme.MyCitiesSearchTheme
import com.example.mycitiessearch.presentation.viewmodels.CitiesViewModel
import kotlinx.coroutines.flow.flowOf

private const val DEFAULT_LIST_INDEX = 0
private const val DEFAULT_WEIGHT = 1f
private const val DEFAULT_LANDSCAPE_WEIGHT = 0.5f

@Composable
fun CitiesRoute(citiesViewModel: CitiesViewModel, navigateToMap : (CityModel) -> Unit) {
    val citiesDB = citiesViewModel.getLocalCities.collectAsLazyPagingItems()

    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation

    if (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
        LandscapeScreen(
            cities = citiesDB,
            searchText = citiesViewModel.textQuery,
            isFavoritesFilter = citiesViewModel.isFavoritesFilter,
            onSearchTextChanged = {
                citiesViewModel.textQuery = it
                citiesViewModel.clearCitiesPagingSource()
            },
            onFilterClicked = {
                citiesViewModel.isFavoritesFilter = it
                citiesViewModel.clearCitiesPagingSource()
            },
            onFavoriteSelected = {
                citiesViewModel.updateCity(it)
            }
        )
    } else {
        PortraitScreen(
            cities = citiesDB,
            searchText = citiesViewModel.textQuery,
            isFavoritesFilter = citiesViewModel.isFavoritesFilter,
            onSearchTextChanged = {
                citiesViewModel.textQuery = it
                citiesViewModel.clearCitiesPagingSource()
            },
            navigateToMap = { navigateToMap(it) },
            onFilterClicked = {
                citiesViewModel.isFavoritesFilter = it
                citiesViewModel.clearCitiesPagingSource()
            },
            onFavoriteSelected = {
                citiesViewModel.updateCity(it)
            }
        )
    }
}

@Composable
fun PortraitScreen(
    cities: LazyPagingItems<CityModel>,
    searchText: String,
    isFavoritesFilter: Boolean,
    onSearchTextChanged: (String) -> Unit,
    navigateToMap: (CityModel) -> Unit,
    onFilterClicked: (Boolean) -> Unit,
    onFavoriteSelected: (CityModel) -> Unit
) {
    CitiesListScreen(
        modifier = Modifier.fillMaxSize(),
        cities = cities,
        searchText = searchText,
        isFavoritesFilter = isFavoritesFilter,
        onSearchTextChanged = onSearchTextChanged,
        navigateToMap = navigateToMap,
        onFilterClicked = onFilterClicked,
        onFavoriteSelected = onFavoriteSelected
    )
}

@Composable
fun LandscapeScreen(
    cities: LazyPagingItems<CityModel>,
    searchText: String,
    isFavoritesFilter: Boolean,
    onSearchTextChanged: (String) -> Unit,
    onFilterClicked: (Boolean) -> Unit,
    onFavoriteSelected: (CityModel) -> Unit
) {
    var citySelected by remember { mutableStateOf(CityModel()) }

    Surface {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CitiesListScreen(
                modifier = Modifier
                    .fillMaxWidth(DEFAULT_LANDSCAPE_WEIGHT)
                    .fillMaxHeight(),
                cities = cities,
                searchText = searchText,
                isFavoritesFilter = isFavoritesFilter,
                onSearchTextChanged = onSearchTextChanged,
                navigateToMap = { citySelected = it },
                onFilterClicked = onFilterClicked,
                onFavoriteSelected = onFavoriteSelected
            )

            Scaffold(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
            ) { innerPadding ->
                GoogleMapsScreen(
                    modifier = Modifier
                        .padding(innerPadding),
                    city = citySelected
                )
            }
        }
    }
}

@Composable
fun CitiesListScreen(
    modifier:Modifier = Modifier,
    cities: LazyPagingItems<CityModel>,
    searchText: String,
    isFavoritesFilter: Boolean,
    onSearchTextChanged: (String) -> Unit,
    navigateToMap: (CityModel) -> Unit,
    onFilterClicked: (Boolean) -> Unit,
    onFavoriteSelected: (CityModel) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            SearchToolbar(
                searchText = searchText,
                isFavoritesFilter = isFavoritesFilter,
                onSearchTextChanged = {
                    onSearchTextChanged(it)
                },
                onFilterClicked = { onFilterClicked(it) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding()
            )
        ) {
            CitiesList(
                cities = cities,
                navigateToMap = { navigateToMap(it) },
                onFavoriteSelected = { onFavoriteSelected(it) }
            )
        }
    }
}

@Composable
fun CitiesList(
    cities: LazyPagingItems<CityModel>,
    navigateToMap: (CityModel) -> Unit,
    onFavoriteSelected: (CityModel) -> Unit
) {
    val lazyColumnListState = rememberLazyListState()

    var showCityDialog by remember { mutableStateOf(false) }
    var citySelected by remember { mutableStateOf(CityModel()) }

    LaunchedEffect(key1 = cities.loadState) {
        lazyColumnListState.animateScrollToItem(DEFAULT_LIST_INDEX)
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = lazyColumnListState,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(dimensionResource(id = R.dimen.size_16dp))
    ) {
        items(
            cities.itemCount,
            key = { cities[it]?.id ?: DEFAULT_LIST_INDEX },
        ) {
            cities[it]?.let { city ->
                CityItem(
                    cityModel = city,
                    onFavoriteSelected = { favoriteCity ->
                        onFavoriteSelected(favoriteCity)
                    },
                    onCitySelected = { citySelected -> navigateToMap(citySelected) },
                    onCityViewSelected = {
                        citySelected = city
                        showCityDialog = true
                    }
                )
            }
        }
    }

    if (showCityDialog){
        CityDetailDialog(
            city = citySelected,
            onDismissRequest = {
                showCityDialog = false
            }
        )
    }
}

@Composable
fun CityItem(
    cityModel: CityModel,
    onFavoriteSelected: (CityModel) -> Unit,
    onCitySelected: (CityModel) -> Unit,
    onCityViewSelected: (CityModel) -> Unit
) {
    var openFavoritesDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(id = R.dimen.size_6dp))
            .clickable { onCitySelected(cityModel) },
        shape = RoundedCornerShape(
            dimensionResource(id = R.dimen.size_12dp)
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(id = R.dimen.size_2dp)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.size_16dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(DEFAULT_WEIGHT)) {
                Text(
                    text = cityModel.name ?: String(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = cityModel.country,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_4dp)))
                Text(
                    text = stringResource(
                        id = R.string.city_item_latitude_and_longitude,
                        cityModel.lat,
                        cityModel.lon
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                IconButton(onClick = {
                    openFavoritesDialog = true
                }) {
                    Icon(
                        imageVector =
                            if (cityModel.isFavorite == true) {
                                Icons.Default.Favorite
                            } else {
                                Icons.Default.FavoriteBorder
                            },
                        contentDescription =
                            stringResource(
                                if (cityModel.isFavorite == true) {
                                    R.string.city_item_content_description_delete_favorites
                                } else {
                                    R.string.city_item_content_description_add_favorites
                                }
                            ),
                        tint =
                            if (cityModel.isFavorite == true) {
                                Color.DarkGray
                            } else {
                                Color.Gray
                            },
                        modifier = Modifier.size(
                            dimensionResource(id = R.dimen.size_28dp)
                        )
                    )
                }

                Spacer(
                    modifier = Modifier.height(
                        dimensionResource(id = R.dimen.size_8dp)
                    )
                )

                Button(
                    onClick = { onCityViewSelected(cityModel) },
                    modifier = Modifier.width(dimensionResource(id = R.dimen.size_90dp)),
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.city_item_btn_see),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }

    if (openFavoritesDialog) {
        ConfirmDialog(
            title = stringResource(R.string.default_dialog_title),
            description = stringResource(
                if (openFavoritesDialog) {
                    R.string.favorite_dialog_disable_text
                } else {
                    R.string.favorite_dialog_enable_text
                },
                cityModel.name ?: String()
            ),
            confirmButtonText = stringResource(R.string.favorite_dialog_enable),
            dismissButtonText = stringResource(R.string.favorite_dialog_disable),
            onConfirm = {
                cityModel.isFavorite = cityModel.isFavorite?.not() ?: false
                onFavoriteSelected(cityModel)
                openFavoritesDialog = false
            },
            onDismiss = { openFavoritesDialog = false }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CityItemPreview() {
    MyCitiesSearchTheme {
        CityItem(
            cityModel = CityModel(
                id = 1,
                name = "City A",
                country = "Country A",
                isFavorite = false,
                lon = 106.200000,
                lat = 36.160000
            ),
            onFavoriteSelected = {},
            onCitySelected = {},
            onCityViewSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CitiesListScreenPreview() {
    val cities =
        listOf(
            CityModel(
                id = 1,
                name = "City A",
                country = "Country A",
                isFavorite = false,
                lon = 106.232341,
                lat = 36.167531
            ),
            CityModel(
                id = 2,
                name = "City B",
                country = "Country B",
                isFavorite = true,
                lon = 1.0,
                lat = 1.0
            )
        )

    MyCitiesSearchTheme {
        CitiesListScreen(
            cities = flowOf(PagingData.from(cities)).collectAsLazyPagingItems(),
            searchText = "",
            isFavoritesFilter = false,
            onSearchTextChanged = {},
            navigateToMap = {},
            onFilterClicked = {},
            onFavoriteSelected = {}
        )
    }
}

@Preview(showBackground = true, device = "spec:parent=pixel_5,orientation=landscape")
@Composable
private fun LandscapeCitiesPreview() {
    val cities =
        listOf(
            CityModel(
                id = 1,
                name = "City A",
                country = "Country A",
                isFavorite = false,
                lon = 106.232341,
                lat = 36.167531
            ),
            CityModel(
                id = 2,
                name = "City B",
                country = "Country B",
                isFavorite = true,
                lon = 1.0,
                lat = 1.0
            )
        )

    MyCitiesSearchTheme {
        LandscapeScreen(
            cities = flowOf(PagingData.from(cities)).collectAsLazyPagingItems(),
            searchText = "",
            isFavoritesFilter = false,
            onSearchTextChanged = {},
            onFilterClicked = {},
            onFavoriteSelected = {}
        )
    }
}
