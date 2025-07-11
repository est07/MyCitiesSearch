package com.example.mycitiessearch.domain.repositories

import com.example.mycitiessearch.data.responses.CityResponse
import kotlinx.coroutines.flow.Flow

interface CitiesRepository {
    suspend fun getCitiesList(): Flow<List<CityResponse>>
}