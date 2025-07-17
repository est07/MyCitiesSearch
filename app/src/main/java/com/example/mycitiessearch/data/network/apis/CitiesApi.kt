package com.example.mycitiessearch.data.network.apis

import com.example.mycitiessearch.data.network.responses.CityResponse
import retrofit2.http.GET

interface CitiesApi {

    @GET("cities.json")
    suspend fun getCitiesList(): List<CityResponse>
}