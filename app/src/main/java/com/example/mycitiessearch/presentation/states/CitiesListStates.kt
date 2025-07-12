package com.example.mycitiessearch.presentation.states

import com.example.mycitiessearch.domain.models.City

sealed class CitiesListStates {
    data class Success(val cities: List<City>) : CitiesListStates()
    data object Default : CitiesListStates()
    data object Loading : CitiesListStates()
    data object Error : CitiesListStates()
}