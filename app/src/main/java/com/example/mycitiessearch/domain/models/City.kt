package com.example.mycitiessearch.domain.models

import com.example.mycitiessearch.data.database.entities.CityEntity
import com.example.mycitiessearch.data.responses.CityResponse

data class City(
    val id: Int,
    val name: String,
    val country: String,
    var isFavorite:Boolean? = false,
    val lon: Double,
    val lat: Double
)

fun CityResponse.toDomain() = City(
    id = id,
    name = name,
    country = country,
    lon = coordinates.lon,
    lat = coordinates.lat
)
fun CityEntity.toDomain() = City(
    id = id,
    name = name,
    country = country,
    isFavorite = isFavorite,
    lon = lon,
    lat = lat
)
