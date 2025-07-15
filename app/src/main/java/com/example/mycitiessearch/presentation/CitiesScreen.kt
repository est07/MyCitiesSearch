package com.example.mycitiessearch.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.mycitiessearch.R
import com.example.mycitiessearch.domain.models.CityModel
import com.example.mycitiessearch.presentation.ui.theme.MyCitiesSearchTheme
import com.example.mycitiessearch.presentation.viewmodels.CitiesViewModel
import kotlinx.coroutines.flow.flowOf

@Composable
fun CitiesRoute(citiesViewModel: CitiesViewModel, navigateToMap : (CityModel) -> Unit) {
    val citiesDB = citiesViewModel.getLocalCities.collectAsLazyPagingItems()

    CitiesListScreen(
        cities = citiesDB,
        searchText = citiesViewModel.textQuery,
        isFavoritesFilter = citiesViewModel.isFavoritesFilter,
        onSearchTextChanged = {
            citiesViewModel.textQuery = it
            citiesViewModel.searchCities()
        },
        navigateToMap = { navigateToMap(it) },
        onFilterClicked = {}
    )
}

@Composable
fun CitiesListScreen(
    cities: LazyPagingItems<CityModel>,
    searchText: String,
    isFavoritesFilter: Boolean,
    onSearchTextChanged: (String) -> Unit,
    navigateToMap: (CityModel) -> Unit,
    onFilterClicked: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SearchToolbar(
                searchText = searchText,
                onSearchTextChanged = {
                    onSearchTextChanged(it)
                },
                onFilterClicked = { }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            CitiesList(
                cities = cities,
                isFavoritesFilter = isFavoritesFilter,
                navigateToMap = { navigateToMap(it) },
                onCitySelected = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchToolbar(
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    onFilterClicked: () -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.size_110dp)),
        title = {
            SearchBar(
                searchText = searchText,
                onSearchTextChanged = onSearchTextChanged,
                onFilterClicked = onFilterClicked
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White
        )
    )
}

@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    onFilterClicked: () -> Unit
) {
    var isTextPresent by remember { mutableStateOf(searchText.isNotEmpty()) }

    OutlinedTextField(
        value = searchText,
        onValueChange = {
            onSearchTextChanged(it)
            isTextPresent = it.isNotEmpty()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = dimensionResource(id = R.dimen.size_8dp),
                horizontal = dimensionResource(id = R.dimen.size_4dp)
            ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(
                    R.string.toolbar_content_description_search_icon
                ),
                tint = Color.Gray
            )
        },
        trailingIcon = {
            Row {
                if (isTextPresent) {
                    IconButton(onClick = {
                        onSearchTextChanged(String())
                        isTextPresent = false
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription =
                                stringResource(
                                    id = R.string.toolbar_content_description_clear_icon
                                ),
                            tint = Color.Gray
                        )
                    }
                }
                IconButton(onClick = onFilterClicked) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_filter_list_24),
                        contentDescription =
                            stringResource(
                                id = R.string.toolbar_content_description_filter_icon
                            ),
                        tint = Color.Gray
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        placeholder = { Text(stringResource(R.string.toolbar_placeholder)) },
        singleLine = true,
        shape = MaterialTheme.shapes.medium,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Gray,
            unfocusedIndicatorColor = Color.Gray,
            disabledIndicatorColor = Color.Gray
        ),
    )
}

@Composable
fun CitiesList(
    cities: LazyPagingItems<CityModel>,
    isFavoritesFilter: Boolean,
    navigateToMap: (CityModel) -> Unit,
    onCitySelected: (CityModel) -> Unit
) {
    val lazyColumnListState = rememberLazyListState()

    var showCityDialog by remember { mutableStateOf(false) }
    var citySelected by remember { mutableStateOf(CityModel()) }

    LaunchedEffect(key1 = cities.loadState) {
        lazyColumnListState.animateScrollToItem(0)
    }

    LazyColumn(
        state = lazyColumnListState,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(dimensionResource(id = R.dimen.size_16dp))
    ) {
        items(
            cities.itemCount,
            key = { cities[it]?.id ?: 0 },
        ) {
            cities[it]?.let { city ->
                CityItem(
                    cityModel = city,
                    onFavoriteClick = {},
                    onCitySelected = { citySelected -> navigateToMap(citySelected) },
                    onCityViewSelected = {
                        citySelected = city
                        onCitySelected(city)
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
    onFavoriteClick: (CityModel) -> Unit,
    onCitySelected: (CityModel) -> Unit,
    onCityViewSelected: (CityModel) -> Unit
) {
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
            Column(modifier = Modifier.weight(1f)) {
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
                    cityModel.isFavorite = cityModel.isFavorite?.not() ?: false
                    onFavoriteClick(cityModel)
                }) {
                    Icon(
                        imageVector =
                            if (cityModel.isFavorite == true) {
                                Icons.Default.Favorite
                            } else {
                                Icons.Default.FavoriteBorder
                            },
                        contentDescription = stringResource(
                            if (cityModel.isFavorite == true) {
                                R.string.city_item_content_description_delete_favorites
                            } else {
                                R.string.city_item_content_description_add_favorites
                            }
                        ),
                        tint = if (cityModel.isFavorite == true) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
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
}

@Composable
fun CityDetailDialog(
    city: CityModel,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    dimensionResource(id = R.dimen.size_16dp)
                ),
            shape = RoundedCornerShape(
                dimensionResource(id = R.dimen.size_16dp)
            ),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.size_8dp))
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(onClick = onDismissRequest) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription =
                                stringResource(
                                    id = R.string.city_dialog_close_icon
                                ),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                Image(
                    painter = painterResource(id = R.drawable.icon_location_city_24),
                    contentDescription = stringResource(R.string.city_dialog_city_image),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.size_150dp))
                        .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

                Text(
                    modifier = Modifier.padding(horizontal =
                        dimensionResource(id = R.dimen.size_16dp)
                    ),
                    text = stringResource(id = R.string.city_item_city_name),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = R.dimen.size_16dp)),
                    text = city.name ?: String(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = R.dimen.size_16dp)),
                    text = stringResource(id = R.string.city_item_country),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = R.dimen.size_16dp)),
                    text = city.country,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                Text(
                    modifier = Modifier.padding(
                        horizontal = dimensionResource(id = R.dimen.size_16dp)
                    ),
                    text = stringResource(
                        id = R.string.city_item_latitude,
                        city.lat
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    modifier = Modifier.padding(
                        horizontal = dimensionResource(id = R.dimen.size_16dp)
                    ),
                    text = stringResource(
                        id = R.string.city_item_longitude,
                        city.lon
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchToolbarPreview() {
    MaterialTheme {
        SearchToolbar(
            searchText = String(),
            onSearchTextChanged = { },
            onFilterClicked = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CityItemPreview() {
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
            onFavoriteClick = {},
            onCitySelected = {},
            onCityViewSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CitiesListScreenPreview() {
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
            isFavoritesFilter = true,
            onSearchTextChanged = {},
            navigateToMap = { },
            onFilterClicked = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CityDetailDialogPreview() {
    MyCitiesSearchTheme {
        CityDetailDialog(
            CityModel(
                id = 1,
                name = "City A",
                country = "Country A",
                isFavorite = false,
                lon = 106.232341,
                lat = 36.167531
            ),
            onDismissRequest = { }
        )
    }
}


