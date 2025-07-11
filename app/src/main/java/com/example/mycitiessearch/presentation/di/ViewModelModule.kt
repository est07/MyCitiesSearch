package com.example.mycitiessearch.presentation.di

import com.example.mycitiessearch.presentation.viewmodels.CitiesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        CitiesViewModel(citiesUseCase = get())
    }
}