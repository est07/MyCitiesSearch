package com.example.mycitiessearch.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.mycitiessearch.R
import com.example.mycitiessearch.domain.models.CityModel
import com.example.mycitiessearch.presentation.states.CitiesListStates
import com.example.mycitiessearch.presentation.ui.theme.MyCitiesSearchTheme
import com.example.mycitiessearch.presentation.viewmodels.CitiesViewModel
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val citiesViewModel: CitiesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            citiesViewModel.getCitiesList()
            MyCitiesSearchTheme {

                val citiesDB = citiesViewModel.getLocalCities.collectAsLazyPagingItems()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        SearchToolbar(
                            searchText = citiesViewModel.textQuery,
                            onSearchTextChanged = {
                                citiesViewModel.textQuery = it
                                citiesViewModel.searchCities()
                            },
                            onFilterClicked = { /*TODO*/ }
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        CitiesListScreen(
                            viewModel = citiesViewModel,
                            cities = citiesDB
                        )
                    }
                }
            }
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
            .height(110.dp)
        ,
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
                .padding(vertical = 8.dp, horizontal = 4.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon",
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
                                contentDescription = "Clear Icon",
                                tint = Color.Gray
                            )
                        }
                    }
                    IconButton(onClick = onFilterClicked) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_filter_list_24),
                            contentDescription = "Filter Icon",
                            tint = Color.Gray
                        )
                    }
                }
            },
            placeholder = { Text("Search") },
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Color.Gray,
                disabledIndicatorColor = Color.Gray
            ),
        )

}

@Preview(showBackground = true)
@Composable
fun SearchToolbarPreview() {
    MaterialTheme {
        SearchToolbar(
            searchText = "",
            onSearchTextChanged = { /*TODO*/ },
            onFilterClicked = { /*TODO*/ }
        )
    }
}

@Composable
fun CitiesListScreen(
    viewModel: CitiesViewModel,
    cities: LazyPagingItems<CityModel>
) {
    val getCitiesListService = viewModel.citiesListState.collectAsStateWithLifecycle()

    when (getCitiesListService.value) {
        CitiesListStates.Loading -> {
            Log.d("", "loading")
        }

        is CitiesListStates.Error -> {
            Log.d("", "error")
        }

        is CitiesListStates.Success -> {
            LaunchedEffect(key1 = getCitiesListService.value) {
                Log.d("", "get local Cities list **")
            }

            CitiesList(
                cities = cities,
                isFavoritesFilter = viewModel.isFavoritesFilter,
                onShowMap = {
                },
                onCitySelected = {}
            )
        }

        else -> Unit
    }
}

@Composable
fun CitiesList(
    cities: LazyPagingItems<CityModel>,
    isFavoritesFilter: Boolean,
    onShowMap : () -> Unit,
    onCitySelected : (CityModel) -> Unit
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
        contentPadding = PaddingValues(16.dp)
    ) {
        items(
            cities.itemCount,
            key = { cities[it]?.id ?: 0 },
        ) {
            cities[it]?.let { city ->
                CityItem(
                    cityModel = city,
                    onCitySelected = {
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
    onCitySelected: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                onCitySelected()
            }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Name: ${cityModel.name}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Country: ${cityModel.country}",
                style = MaterialTheme.typography.bodyMedium
            )
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
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(onClick = onDismissRequest) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Image(
                    painter = painterResource(id = R.drawable.icon_location_city_24),
                    contentDescription = "Imagen de ${city.name}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    text = "Ciudad: ",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    text = city.name ?: String(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    text = "Country: ",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    text = city.country,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Latitud: ${city.lat}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Longitud: ${city.lon}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CitiesListPreview() {
    val cities =
        listOf(
            CityModel(
                id = 1,
                name = "City A",
                country = "Country A",
                isFavorite = false,
                lon = 0.0,
                lat = 0.0
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
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                CitiesList(
                    cities = flowOf(PagingData.from(cities)).collectAsLazyPagingItems(),
                    isFavoritesFilter = true,
                    onShowMap = {},
                    onCitySelected = {}
                )
            }
        }
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
                lon = 106.200000,
                lat = 36.160000
            ),
            onDismissRequest = { }
        )
    }
}