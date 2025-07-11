package com.example.mycitiessearch.presentation

import android.app.Application
import com.example.mycitiessearch.presentation.di.repositoriesModule
import com.example.mycitiessearch.presentation.di.serviceModule
import com.example.mycitiessearch.presentation.di.useCasesModule
import com.example.mycitiessearch.presentation.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

class MyCitiesSearchApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyCitiesSearchApplication)
            loadKoinModules(
                listOf(
                    serviceModule,
                    repositoriesModule,
                    useCasesModule,
                    viewModelModule
                )
            )
        }
    }
}