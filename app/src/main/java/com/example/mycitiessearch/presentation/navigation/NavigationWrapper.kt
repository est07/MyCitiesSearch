package com.example.mycitiessearch.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mycitiessearch.domain.models.CityModel
import com.example.mycitiessearch.presentation.compose.CitiesMapScreen
import com.example.mycitiessearch.presentation.compose.CitiesRoute
import com.example.mycitiessearch.presentation.navigation.type.createNavType
import com.example.mycitiessearch.presentation.viewmodels.CitiesViewModel
import kotlin.reflect.typeOf

@Composable
fun NavigationWrapper(citiesViewModel: CitiesViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = CitiesScreen) {
        composable<CitiesScreen> {
            CitiesRoute(
                citiesViewModel = citiesViewModel,
                navigateToMap = { navController.navigate(MapScreen(it)) }
            )
        }

        composable<MapScreen>(
            typeMap = mapOf(typeOf<CityModel>() to createNavType<CityModel>())
        ) { navBackStackEntry ->
            val mapData: MapScreen = navBackStackEntry.toRoute()
            CitiesMapScreen(city = mapData.city)
        }
    }
}