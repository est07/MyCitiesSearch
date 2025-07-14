package com.example.mycitiessearch.domain.repositories

import com.example.mycitiessearch.data.database.entities.CityEntity
import com.example.mycitiessearch.domain.models.CityModel
import kotlinx.coroutines.flow.Flow

interface CitiesRepository {
    suspend fun getCitiesList(): Flow<List<CityModel>>

    suspend fun insertCitiesListDB(cities: List<CityEntity>)

    suspend fun getCitiesDB(limit: Int, offset: Int): List<CityModel>

    suspend fun getSearchFavoritesCities(query: String, limit: Int, offset: Int): List<CityModel>

    suspend fun getCitiesSearch(query: String, limit: Int, offset: Int): List<CityModel>

    suspend fun getAllFavoritesCities(limit: Int, offset: Int): List<CityModel>
}