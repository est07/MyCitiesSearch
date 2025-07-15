package com.example.mycitiessearch.data.repositories

import com.example.mycitiessearch.data.database.dao.CitiesDao
import com.example.mycitiessearch.data.database.entities.CityEntity
import com.example.mycitiessearch.data.database.entities.toDatabase
import com.example.mycitiessearch.data.network.apis.CitiesApi
import com.example.mycitiessearch.data.responses.CityResponse
import com.example.mycitiessearch.domain.models.CityModel
import com.example.mycitiessearch.domain.models.toDomain
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CitiesRepositoryImplTest {

    private val api = mockk<CitiesApi>()
    private val citiesDao = mockk<CitiesDao>()

    private lateinit var citiesRepositoryImpl: CitiesRepositoryImpl

    @Before
    fun setUp() {
        citiesRepositoryImpl = CitiesRepositoryImpl(api = api, citiesDao = citiesDao)
    }

    @Test
    fun getCitiesApiTest(): Unit = runBlocking {

        val citiesResponse =  mockk<List<CityResponse>>(relaxed = true)

        coEvery { api.getCitiesList() } returns citiesResponse

        citiesRepositoryImpl.getCitiesList().collect {
            assert(it == citiesResponse.map { cities -> cities.toDomain() })
        }
        coVerify { api.getCitiesList() }

        confirmVerified(api)
    }

    @Test
    fun insertCitiesListDBTest() = runBlocking {
        val cities = listOf(
            CityModel(
                id = 1,
                name = "Test City",
                country = "Test Country",
                isFavorite = false,
                lat = 0.0,
                lon = 0.0
            ),
            CityModel(
                id = 2,
                name = "Test City 2",
                country = "Test Country 2",
                isFavorite = false,
                lat = 0.1,
                lon = 0.1
            )
        )

        coEvery {
            citiesDao.insertAll(
                cities.map { it.toDatabase() }
            )
        } returns Unit

        coEvery {
            citiesDao.getAllCities(
                limit = 5,
                offset = 0
            )
        } returns cities.map { it.toDatabase() }

        citiesRepositoryImpl.insertCitiesListDB(cities.map { it.toDatabase() })

        val citiesDB = citiesRepositoryImpl.getCitiesDB(limit = 5, offset = 0)

        assertEquals(cities , citiesDB)
        coVerify {
            citiesDao.insertAll(
                cities.map { it.toDatabase() }
            )
            citiesDao.getAllCities(
                limit = 5,
                offset = 0
            )
        }
        confirmVerified(citiesDao)
    }

    @Test
    fun getCitiesAllCitiesDBTest(): Unit = runBlocking {

        val cities =  mockk<List<CityEntity>>(relaxed = true)

        coEvery { citiesDao.getAllCities(limit = 5, offset = 0) } returns cities

        val citiesDB = citiesRepositoryImpl.getCitiesDB(limit = 5, offset = 0)

        assertEquals(citiesDB , cities.map { it.toDomain() })

        coVerify { citiesDao.getAllCities(limit = 5, offset = 0) }

        confirmVerified(citiesDao)
    }

    @Test
    fun getSearchFavoritesCitiesTest(): Unit = runBlocking {

        val cities =  mockk<List<CityEntity>>(relaxed = true)

        coEvery {
            citiesDao.getSearchFavoritesCities(
                query = "test",
                limit = 5,
                offset = 0
            )
        } returns cities

        val citiesDB =
            citiesRepositoryImpl.getSearchFavoritesCities(query = "test", limit = 5, offset = 0)

        assertEquals(citiesDB , cities.map { it.toDomain() })

        coVerify { citiesDao.getSearchFavoritesCities(query = "test", limit = 5, offset = 0) }

        confirmVerified(citiesDao)
    }


    @Test
    fun getCitiesSearchTest(): Unit = runBlocking {

        val cities =  mockk<List<CityEntity>>(relaxed = true)

        coEvery {
            citiesDao.getCitiesSearch(
                query = "test",
                limit = 5,
                offset = 0
            )
        } returns cities

        val citiesDB =
            citiesRepositoryImpl.getCitiesSearch(query = "test", limit = 5, offset = 0)

        assertEquals(citiesDB , cities.map { it.toDomain() })

        coVerify { citiesDao.getCitiesSearch(query = "test", limit = 5, offset = 0) }

        confirmVerified(citiesDao)
    }

    @Test
    fun allFavoritesCitiesTest(): Unit = runBlocking {

        val cities =  mockk<List<CityEntity>>(relaxed = true)

        coEvery {
            citiesDao.getAllFavoritesCities(
                limit = 5,
                offset = 0
            )
        } returns cities

        val citiesDB =
            citiesRepositoryImpl.getAllFavoritesCities(limit = 5, offset = 0)

        assertEquals(citiesDB , cities.map { it.toDomain() })

        coVerify { citiesDao.getAllFavoritesCities(limit = 5, offset = 0) }

        confirmVerified(citiesDao)
    }

    @Test
    fun updateCityTest() = runBlocking {
        val city =
            CityEntity(
                id = 1,
                name = "Test City",
                country = "Test Country",
                isFavorite = false,
                lat = 0.0,
                lon = 0.0
            )

        coEvery {
            citiesDao.updateCity(city)
        } returns Unit

        val updateCityDB = citiesRepositoryImpl.updateCity(city)

        assertEquals(Unit , updateCityDB)
        coVerify {
            citiesDao.updateCity(city)
        }
        confirmVerified(citiesDao)
    }
}