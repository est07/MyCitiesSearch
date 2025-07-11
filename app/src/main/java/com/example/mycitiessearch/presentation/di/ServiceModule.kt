package com.example.mycitiessearch.presentation.di

import com.example.mycitiessearch.data.apis.CitiesApi
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val serviceModule = module {
    single(named(RETROFIT_API)) { ManagerNetwork().createWebService() }
    single { get<Retrofit>(named(RETROFIT_API)).create(CitiesApi::class.java) }
}