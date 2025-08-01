package com.example.mycitiessearch.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mycitiessearch.presentation.paging.CitiesPagingSource
import com.example.mycitiessearch.domain.models.CityModel
import com.example.mycitiessearch.domain.usecases.CitiesUseCase
import com.example.mycitiessearch.presentation.states.CitiesListStates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

private const val MAX_ITEMS = 20
private const val PREFETCH_ITEMS = 5

class CitiesViewModel(
    private val citiesUseCase: CitiesUseCase,
) : ViewModel() {

    private val _citiesListState =
        MutableStateFlow<CitiesListStates>(CitiesListStates.Default)

    val citiesListState: StateFlow<CitiesListStates>
        get() = _citiesListState

    var textQuery by mutableStateOf(String())

    var isFavoritesFilter by mutableStateOf(false)

    private var pagingSource: CitiesPagingSource? = null
        get() {
            if (field == null || field?.invalid == true) {
                field = CitiesPagingSource(
                    textQuery = textQuery,
                    isFavoritesFilter = isFavoritesFilter,
                    citiesUseCase = citiesUseCase
                )
            }

            return field
        }

    val getLocalCities: Flow<PagingData<CityModel>> =
        Pager(
            config = PagingConfig(pageSize = MAX_ITEMS, prefetchDistance = PREFETCH_ITEMS),
            pagingSourceFactory = {
                pagingSource!!
            }
        ).flow.cachedIn(viewModelScope)

    fun getCitiesList() {
        viewModelScope.launch {
            citiesUseCase.getCitiesList()
                .onStart {
                    _citiesListState.value = CitiesListStates.Loading
                }
                .catch {
                    _citiesListState.value = CitiesListStates.Error
                }
                .collect {
                    _citiesListState.value = CitiesListStates.Success
                }
        }
    }

    fun clearCitiesPagingSource() {
        pagingSource?.invalidate()
    }

    fun updateCity(city: CityModel) {
        viewModelScope.launch {
            citiesUseCase.updateCity(city)
        }
    }
}