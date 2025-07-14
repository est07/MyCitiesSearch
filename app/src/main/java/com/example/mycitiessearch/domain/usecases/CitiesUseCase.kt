package com.example.mycitiessearch.domain.usecases

import com.example.mycitiessearch.data.database.entities.toDatabase
import com.example.mycitiessearch.domain.repositories.CitiesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

class CitiesUseCase(
    private val citiesRepository: CitiesRepository,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getCitiesList() = withContext(ioDispatcher) {
        citiesRepository.getCitiesList().onEach { cities ->
            citiesRepository.insertCitiesListDB(cities.map { it.toDatabase() })
        }
    }

    suspend fun getCitiesDB(limit: Int, offset: Int) = withContext(ioDispatcher) {
        citiesRepository.getCitiesDB(limit, offset)
    }

    suspend fun getSearchFavoritesCities(query: String, limit: Int, offset: Int) =
        withContext(ioDispatcher) {
            citiesRepository.getSearchFavoritesCities(query = query, limit = limit, offset = offset)
        }

    suspend fun getCitiesSearch(query: String, limit: Int, offset: Int) =
        withContext(ioDispatcher) {
            citiesRepository.getCitiesSearch(query, limit, offset)
        }

    suspend fun getAllFavoritesCities(limit: Int, offset: Int) = withContext(ioDispatcher) {
        citiesRepository.getAllFavoritesCities(limit, offset)
    }
}