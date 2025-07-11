package com.example.mycitiessearch.data.apis

import com.example.mycitiessearch.data.responses.CityResponse
import retrofit2.http.GET

interface CitiesApi {

    @GET("cities.json")
    suspend fun getCitiesList(): List<CityResponse>
}