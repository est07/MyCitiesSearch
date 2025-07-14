package com.example.mycitiessearch.presentation.di

import androidx.room.Room
import com.example.mycitiessearch.data.database.CitiesDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val CITIES_DATABASE_NAME = "cities_database"

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            CitiesDatabase::class.java,
            CITIES_DATABASE_NAME
        ).build()
    }
    single { get<CitiesDatabase>().getCitiesDao() }
}