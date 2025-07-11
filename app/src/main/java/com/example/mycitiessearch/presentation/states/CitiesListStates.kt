package com.example.mycitiessearch.presentation.states

import com.example.mycitiessearch.data.responses.CityResponse

sealed class CitiesListStates {
    data class Success(val cities: List<CityResponse>) : CitiesListStates()
    data object Default : CitiesListStates()// Assuming cities are represented as a list of strings
    data object Loading : CitiesListStates()
    data object Error : CitiesListStates()
}