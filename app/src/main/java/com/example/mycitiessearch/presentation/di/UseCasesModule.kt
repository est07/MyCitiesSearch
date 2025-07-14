package com.example.mycitiessearch.presentation.di

import com.example.mycitiessearch.domain.usecases.CitiesUseCase
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val useCasesModule = module {
    factory {
        CitiesUseCase(
            citiesRepository = get(),
            ioDispatcher = Dispatchers.IO
        )
    }
}