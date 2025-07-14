package com.example.mycitiessearch.domain.models

import com.example.mycitiessearch.data.database.entities.CityEntity
import com.example.mycitiessearch.data.responses.CityResponse

data class CityModel(
    val id: Int,
    val name: String,
    val country: String,
    var isFavorite:Boolean? = false,
    val lon: Double,
    val lat: Double
)

fun CityResponse.toDomain() = CityModel(
    id = id,
    name = name,
    country = country,
    lon = coordinates.lon,
    lat = coordinates.lat
)
fun CityEntity.toDomain() = CityModel(
    id = id,
    name = name,
    country = country,
    isFavorite = isFavorite,
    lon = lon,
    lat = lat
)
