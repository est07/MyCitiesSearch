package com.example.mycitiessearch.data.responses

import com.squareup.moshi.Json

data class CityResponse(
    @Json(name = "country")
    val country: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "_id")
    val id: Int,
    @Json(name = "coord")
    val coordinates: Coordinates
)

data class Coordinates(
    @Json(name = "lon")
    val lon: Double,
    @Json(name = "lat")
    val lat: Double
)