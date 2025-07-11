package com.example.mycitiessearch.data.repositories

import com.example.mycitiessearch.data.apis.CitiesApi
import com.example.mycitiessearch.data.responses.CityResponse
import com.example.mycitiessearch.domain.repositories.CitiesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CitiesRepositoryImpl(private val api: CitiesApi): CitiesRepository {

    override suspend fun getCitiesList(): Flow<List<CityResponse>> =
        flow {
            emit(api.getCitiesList())
        }
}