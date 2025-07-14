package com.example.mycitiessearch.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mycitiessearch.domain.models.CityModel

@Entity(tableName = "cities_table")
data class CityEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean,
    @ColumnInfo(name = "lon") val lon: Double,
    @ColumnInfo(name = "lat") val lat: Double
)

fun CityModel.toDatabase() = CityEntity(
    id = id ?: 0,
    name = name ?: String(),
    country = country,
    isFavorite = isFavorite?: false,
    lon = lon,
    lat = lat
)