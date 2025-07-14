package com.example.mycitiessearch.domain.usecases

import com.example.mycitiessearch.data.database.entities.toDatabase
import com.example.mycitiessearch.domain.models.CityModel
import com.example.mycitiessearch.domain.repositories.CitiesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class CitiesUseCaseTest {

    private val citiesRepository = mockk<CitiesRepository>()
    private val ioDispatcher = Dispatchers.IO

    private lateinit var citiesUseCase: CitiesUseCase


    @Before
    fun setUp() {
        citiesUseCase = CitiesUseCase(
            citiesRepository = citiesRepository,
            ioDispatcher = ioDispatcher
        )
    }

    @Test
    fun getCitiesListTest(): Unit = runBlocking {
        val cities =  listOf(
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
            citiesRepository.getCitiesList()
        } returns flowOf(cities)

        coEvery {
            citiesRepository.insertCitiesListDB(cities.map { it.toDatabase() })
        } returns Unit

        val result = citiesUseCase.getCitiesList()

        launch {
            result.onEach {
                citiesRepository.insertCitiesListDB(cities.map { it.toDatabase() })
            }.collect{}
        }

        coVerify {
            citiesRepository.getCitiesList()
            citiesRepository.insertCitiesListDB(cities.map { it.toDatabase() })
        }
    }

    @Test
    fun getCitiesDBTest(): Unit = runBlocking {
        val cities =  mockk<List<CityModel>>(relaxed = true)

        coEvery {
            citiesRepository.getCitiesDB(
                limit = 5,
                offset = 0
            )
        } returns cities

        citiesUseCase.getCitiesDB(
            limit = 5,
            offset = 0
        )

        coVerify {
            citiesRepository.getCitiesDB(limit = 5, offset = 0)
        }
        confirmVerified(citiesRepository)
    }

    @Test
    fun getSearchFavoritesCitiesTest(): Unit = runBlocking {
        val cities =  mockk<List<CityModel>>(relaxed = true)

        coEvery {
            citiesRepository.getSearchFavoritesCities(
                query = "Test",
                limit = 5,
                offset = 0
            )
        } returns cities

        citiesUseCase.getSearchFavoritesCities(
            query = "Test",
            limit = 5,
            offset = 0
        )

        coVerify {
            citiesRepository.getSearchFavoritesCities(
                query = "Test",
                limit = 5,
                offset = 0
            )
        }
        confirmVerified(citiesRepository)
    }

    @Test
    fun getCitiesSearchTest(): Unit = runBlocking {

        val cities =  mockk<List<CityModel>>(relaxed = true)

        coEvery {
            citiesRepository.getCitiesSearch(
                query = "Test",
                limit = 5,
                offset = 0
            )
        } returns cities

        citiesUseCase.getCitiesSearch(
            query = "Test",
            limit = 5,
            offset = 0
        )

        coVerify {
            citiesRepository.getCitiesSearch(
                query = "Test",
                limit = 5,
                offset = 0
            )
        }
        confirmVerified(citiesRepository)
    }

    @Test
    fun getAllFavoritesCities(): Unit = runBlocking {
        val cities =  mockk<List<CityModel>>(relaxed = true)

        coEvery {
            citiesRepository.getAllFavoritesCities(
                limit = 5,
                offset = 0
            )
        } returns cities

        citiesUseCase.getAllFavoritesCities(
            limit = 5,
            offset = 0
        )

        coVerify {
            citiesRepository.getAllFavoritesCities(limit = 5, offset = 0)
        }
        confirmVerified(citiesRepository)
    }
}