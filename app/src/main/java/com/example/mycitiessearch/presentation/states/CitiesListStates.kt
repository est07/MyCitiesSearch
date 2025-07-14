package com.example.mycitiessearch.presentation.states

sealed class CitiesListStates {
    data object Success : CitiesListStates()
    data object Default : CitiesListStates()
    data object Loading : CitiesListStates()
    data object Error : CitiesListStates()
}