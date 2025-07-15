package com.example.mycitiessearch.data.repositories

import com.example.mycitiessearch.data.database.dao.CitiesDao
import com.example.mycitiessearch.data.database.entities.CityEntity
import com.example.mycitiessearch.data.database.entities.toDatabase
import com.example.mycitiessearch.data.network.apis.CitiesApi
import com.example.mycitiessearch.domain.models.CityModel
import com.example.mycitiessearch.domain.models.toDomain
import com.example.mycitiessearch.domain.repositories.CitiesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CitiesRepositoryImpl(
    private val api: CitiesApi,
    private val citiesDao: CitiesDao
) : CitiesRepository {

    override suspend fun getCitiesList(): Flow<List<CityModel>> =
        flow {
            emit(api.getCitiesList().map { it.toDomain() })
        }

    override suspend fun insertCitiesListDB(cities: List<CityEntity>) {
        citiesDao.insertAll(cities)
    }

    override suspend fun getCitiesDB(limit: Int, offset: Int): List<CityModel> =
        citiesDao.getAllCities(limit = limit, offset = offset).map { it.toDomain() }

    override suspend fun getSearchFavoritesCities(
        query: String,
        limit: Int,
        offset: Int
    ): List<CityModel> =
        citiesDao.getSearchFavoritesCities(query = query, limit = limit, offset = offset)
            .map { it.toDomain() }

    override suspend fun getCitiesSearch(query: String, limit: Int, offset: Int): List<CityModel> =
        citiesDao.getCitiesSearch(query = query, limit = limit, offset = offset)
            .map { it.toDomain() }

    override suspend fun getAllFavoritesCities(limit: Int, offset: Int): List<CityModel> =
        citiesDao.getAllFavoritesCities(limit = limit, offset = offset).map { it.toDomain() }

    override suspend fun updateCity(city: CityEntity) {
        citiesDao.updateCity(city)
    }
}