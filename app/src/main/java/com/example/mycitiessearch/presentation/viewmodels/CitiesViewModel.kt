package com.example.mycitiessearch.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycitiessearch.domain.usecases.CitiesUseCase
import com.example.mycitiessearch.presentation.states.CitiesListStates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CitiesViewModel(
    private val citiesUseCase: CitiesUseCase,
) : ViewModel() {

    private val _citiesListState
        get() = MutableStateFlow<CitiesListStates>(CitiesListStates.Default)

    val citiesListState: StateFlow<CitiesListStates>
        get() = _citiesListState.asStateFlow()

    fun getCitiesList() {
        viewModelScope.launch {
            citiesUseCase.getCitiesList()
                .onStart {
                    Log.d("", "Loading cities list")
                    _citiesListState.value = CitiesListStates.Loading }
                .catch {
                    Log.d("", "Error")
                    _citiesListState.value = CitiesListStates.Error
                }
                .collect {
                    Log.d("", "Cities list: $it")
                    _citiesListState.value = CitiesListStates.Success(it)
                }
        }
    }

}