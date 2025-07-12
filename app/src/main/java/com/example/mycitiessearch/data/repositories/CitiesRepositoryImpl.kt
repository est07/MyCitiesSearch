package com.example.mycitiessearch.data.repositories

import com.example.mycitiessearch.data.database.dao.CitiesDao
import com.example.mycitiessearch.data.database.entities.CityEntity
import com.example.mycitiessearch.data.network.apis.CitiesApi
import com.example.mycitiessearch.domain.models.City
import com.example.mycitiessearch.domain.models.toDomain
import com.example.mycitiessearch.domain.repositories.CitiesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CitiesRepositoryImpl(
    private val api: CitiesApi,
    private val citiesDao: CitiesDao
) : CitiesRepository {

    override suspend fun getCitiesList(): Flow<List<City>> =
        flow {
            emit(api.getCitiesList().map { it.toDomain() })
        }


    override suspend fun insertCitiesListDB(cities: List<CityEntity>) {
        citiesDao.insertAll(cities)
    }

    override suspend fun getCitiesListDB(): Flow<List<City>> =
        flow {
            emit(citiesDao.getAllCities().map { it.toDomain() })
        }

}