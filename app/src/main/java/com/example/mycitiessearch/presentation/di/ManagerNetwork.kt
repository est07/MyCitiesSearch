package com.example.mycitiessearch.presentation.di
import com.example.mycitiessearch.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

private const val REQUEST_URL_TEXT = "Request Url:"
private const val RESPONSE_CODE_TEXT = "Response Code:"
private const val RESPONSE_BODY_TEXT = "Response Body:"
private const val DEFAULT_BYTE_COUNT = 2048L
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
        if (BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(HttpInterceptor())
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

class HttpInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val newRequest: Request = request.newBuilder()
            .build()
        println("$REQUEST_URL_TEXT ${request.url}")

        val response: Response = chain.proceed(newRequest)

        println("$RESPONSE_CODE_TEXT ${response.code}")
        println("$RESPONSE_BODY_TEXT ${response.peekBody(DEFAULT_BYTE_COUNT).string()}")

        return response
    }
}