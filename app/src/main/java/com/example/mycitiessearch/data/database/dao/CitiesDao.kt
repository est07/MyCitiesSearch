package com.example.mycitiessearch.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mycitiessearch.data.database.entities.CityEntity

@Dao
interface CitiesDao {
    @Query("SELECT * FROM cities_table ORDER BY name ASC")
    suspend fun getAllCities(): List<CityEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(cities: List<CityEntity>)

    @Update
    suspend fun updateCity(city: CityEntity)

    @Query("DELETE FROM cities_table")
    suspend fun deleteAllCities()
}