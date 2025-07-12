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
    val coordinates: CoordinatesResponse
)

data class CoordinatesResponse(
    @Json(name = "lon")
    val lon: Double,
    @Json(name = "lat")
    val lat: Double
)