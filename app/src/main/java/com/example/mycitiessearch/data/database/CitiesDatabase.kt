package com.example.mycitiessearch.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mycitiessearch.data.database.dao.CitiesDao
import com.example.mycitiessearch.data.database.entities.CityEntity

@Database(entities = [CityEntity::class], version = 1)
abstract class CitiesDatabase: RoomDatabase() {
    abstract fun getCitiesDao():CitiesDao
}