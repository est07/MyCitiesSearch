package com.example.mycitiessearch.presentation.navigation

import com.example.mycitiessearch.domain.models.CityModel
import kotlinx.serialization.Serializable

@Serializable
object CitiesScreen

@Serializable
data class MapScreen(val city:CityModel)