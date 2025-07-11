package com.example.mycitiessearch.domain.usecases

import com.example.mycitiessearch.domain.repositories.CitiesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CitiesUseCase(
    private val citiesRepository: CitiesRepository,
    private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend fun getCitiesList() = withContext(coroutineDispatcher) {
        citiesRepository.getCitiesList()
    }
}