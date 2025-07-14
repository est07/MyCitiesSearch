package com.example.mycitiessearch.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mycitiessearch.data.database.entities.CityEntity

@Dao
interface CitiesDao {
    @Query("SELECT * FROM cities_table ORDER BY name ASC  LIMIT :limit OFFSET :offset")
    suspend fun getAllCities(limit: Int, offset: Int): List<CityEntity>

    @Query(
        "SELECT * FROM cities_table WHERE name LIKE :query || '%'" +
                " ORDER BY name ASC  LIMIT :limit OFFSET :offset"
    )
    suspend fun getCitiesSearch(query: String, limit: Int, offset: Int): List<CityEntity>

    @Query(
        "SELECT * FROM cities_table WHERE is_favorite = 1" +
                " ORDER BY name ASC  LIMIT :limit OFFSET :offset"
    )
    suspend fun getAllFavoritesCities(limit: Int, offset: Int): List<CityEntity>

    @Query(
        "SELECT * FROM cities_table WHERE name LIKE :query || '%' AND is_favorite = 1" +
                " ORDER BY name ASC  LIMIT :limit OFFSET :offset"
    )
    suspend fun getSearchFavoritesCities(query: String, limit: Int, offset: Int): List<CityEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(cities: List<CityEntity>)

    @Update
    suspend fun updateCity(city: CityEntity)

    @Query("DELETE FROM cities_table")
    suspend fun deleteAllCities()
}