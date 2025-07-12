package com.example.mycitiessearch.domain.repositories

import com.example.mycitiessearch.data.database.entities.CityEntity
import com.example.mycitiessearch.data.responses.CityResponse
import com.example.mycitiessearch.domain.models.City
import kotlinx.coroutines.flow.Flow

interface CitiesRepository {
    suspend fun getCitiesList(): Flow<List<City>>

    suspend fun insertCitiesListDB(cities:List<CityEntity>)

    suspend fun getCitiesListDB(): Flow<List<City>>
}