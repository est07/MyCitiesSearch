package com.example.mycitiessearch.presentation.di

import com.example.mycitiessearch.data.repositories.CitiesRepositoryImpl
import com.example.mycitiessearch.domain.repositories.CitiesRepository
import org.koin.dsl.module

val repositoriesModule = module {
    factory<CitiesRepository> { CitiesRepositoryImpl(api = get(), citiesDao = get()) }
}