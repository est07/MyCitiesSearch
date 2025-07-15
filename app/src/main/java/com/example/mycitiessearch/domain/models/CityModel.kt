package com.example.mycitiessearch.domain.models

import android.os.Parcelable
import com.example.mycitiessearch.data.database.entities.CityEntity
import com.example.mycitiessearch.data.responses.CityResponse
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class CityModel(
    val id: Int? = 0,
    val name: String? = "",
    val country: String = "",
    var isFavorite: Boolean? = false,
    val lon: Double = 0.0,
    val lat: Double = 0.0
) : Parcelable

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
