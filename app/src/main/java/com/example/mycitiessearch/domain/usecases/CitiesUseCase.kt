package com.example.mycitiessearch.domain.usecases

import com.example.mycitiessearch.data.database.entities.toDatabase
import com.example.mycitiessearch.domain.repositories.CitiesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
class CitiesUseCase(
    private val citiesRepository: CitiesRepository,
    private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend fun getCitiesList() = withContext(coroutineDispatcher) {

        citiesRepository.getCitiesList().map {cities ->
            citiesRepository.insertCitiesListDB(cities.map { it.toDatabase() })
        }.flatMapConcat { citiesRepository.getCitiesListDB() }
    }
}