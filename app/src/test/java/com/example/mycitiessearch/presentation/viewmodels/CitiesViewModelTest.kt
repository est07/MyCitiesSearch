package com.example.mycitiessearch.presentation.viewmodels

import com.example.mycitiessearch.MainDispatcherRule
import com.example.mycitiessearch.domain.models.CityModel
import com.example.mycitiessearch.domain.usecases.CitiesUseCase
import com.example.mycitiessearch.presentation.states.CitiesListStates
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CitiesViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val citiesUseCaseMock = mockk<CitiesUseCase>()

    private lateinit var viewModel: CitiesViewModel

    @Before
    fun setUp() {
        viewModel = CitiesViewModel(citiesUseCaseMock)
    }

    @Test
    fun citiesListStateTest(): Unit = runBlocking {
        assertEquals(
            viewModel.citiesListState.value,
            CitiesListStates.Default
        )
    }

    @Test
    fun textQueryTest(): Unit = runBlocking {
        viewModel.textQuery = "test"

        assertEquals(viewModel.textQuery, "test")
    }

    @Test
    fun isFavoritesFilterTest(): Unit = runBlocking {
        viewModel.isFavoritesFilter = true

        assertTrue(viewModel.isFavoritesFilter)
    }

    @Test
    fun getLocalCitiesTest(): Unit = runBlocking {
       //TODO This test don´t work for the PagingData
    }

    @Test
    fun getLocalCities_response_success_test(): Unit = runBlocking {
        val cities =  mockk<List<CityModel>>(relaxed = true)

        coEvery {
            citiesUseCaseMock.getCitiesList()
        } returns flowOf(cities)

        viewModel.getCitiesList()

        assertEquals(viewModel.citiesListState.value, CitiesListStates.Success)

        coVerify {
            citiesUseCaseMock.getCitiesList()
        }
        confirmVerified(citiesUseCaseMock)
    }

    @Test
    fun getLocalCities_response_error_test(): Unit = runBlocking {
        val throwable = mockk<Throwable>()
        val flow = flow<List<CityModel>> { throw throwable }

        coEvery {
            citiesUseCaseMock.getCitiesList()
        } returns flow

        viewModel.getCitiesList()

        assertEquals(viewModel.citiesListState.value, CitiesListStates.Error)

        coVerify {
            citiesUseCaseMock.getCitiesList()
        }
        confirmVerified(citiesUseCaseMock)
    }
}