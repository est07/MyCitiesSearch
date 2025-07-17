package com.example.mycitiessearch.presentation.di

import com.example.mycitiessearch.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val RETROFIT_API = "retrofitApi"

class ManagerNetwork {

    private fun getOkHttpClientBuilder(): OkHttpClient {

        val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val defaultRequest = chain.request()
                val defaultHttpUrl = defaultRequest.url
                val httpUrl = defaultHttpUrl.newBuilder().build()
                val requestBuilder = defaultRequest.newBuilder().url(httpUrl)
                chain.proceed(requestBuilder.build())
            }
        return okHttpClientBuilder.build()
    }

    fun createWebService(): Retrofit {
        val moshi: Moshi = Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.JSON_URL_API)
            .client(getOkHttpClientBuilder())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}