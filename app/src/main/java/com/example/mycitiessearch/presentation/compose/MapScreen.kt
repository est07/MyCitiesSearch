package com.example.mycitiessearch.presentation.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.mycitiessearch.domain.models.CityModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

private const val DEFAULT_MAP_ZOOM = 15f
private const val DEFAULT_MAX_LINES = 1
private const val INVALID_CITY_ID = 0

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesMapScreen(
    modifier: Modifier = Modifier,
    city: CityModel,
    navigateBack: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "City Map View",
                        maxLines = DEFAULT_MAX_LINES
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigateBack()}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        GoogleMapsScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            city = city
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun GoogleMapsScreen(modifier: Modifier = Modifier, city: CityModel) {
    val marker = LatLng(city.lat, city.lon)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(marker, DEFAULT_MAP_ZOOM)
    }

    LaunchedEffect(city) {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(marker, DEFAULT_MAP_ZOOM)
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState =
            if (city.id != INVALID_CITY_ID) {
                cameraPositionState
            } else {
                rememberCameraPositionState()
            }
    ) {
        if (city.id != INVALID_CITY_ID) {
            Marker(
                state = MarkerState(position = marker),
                title = city.name, snippet = city.country
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CitiesMapPreview() {
    CitiesMapScreen(
        city = CityModel(
            id = 1,
            name = "City A",
            country = "Country A",
            isFavorite = false,
            lon = 106.232341,
            lat = 36.167531
        ),
        navigateBack = {}
    )
}
