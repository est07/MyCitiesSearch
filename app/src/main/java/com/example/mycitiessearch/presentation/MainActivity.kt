package com.example.mycitiessearch.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
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
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        CitiesListScreen(viewModel = citiesViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun CitiesListScreen(viewModel: CitiesViewModel) {
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

            val cities = viewModel.getLocalCities.collectAsLazyPagingItems()

            CitiesList(
                cities = cities,
                isFavoritesFilter = viewModel.isFavoritesFilter,
                onShowMap = {
                },
                onCitySelected = {
                }
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
    onCitySelected : () -> Unit
) {
    val lazyColumnListState = rememberLazyListState()

    LazyColumn(
        state = lazyColumnListState,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(16.dp)
    ) {
        items(
            cities.itemCount,
            key = { cities[it]?.id ?: 0 },
        ) {
            cities[it]?.let { city -> CityItem(cityModel = city) }
        }
    }
}

@Composable
fun CityItem(cityModel: CityModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
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