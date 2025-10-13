package com.example.tastebudge

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// To interact with REST api (yelp)
object RetrofitClient {
    private const val BASE_URL: String = "https://api.yelp.com/v3/"
    private const val API_KEY: String = BuildConfig.YELP_API_KEY
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Client that sends HTTP requests and reads the responses
    private val okHttpClient = OkHttpClient.Builder()
        // Add headers to request
        .addInterceptor { chain ->
            val original: Request = chain.request()
            val requestBuilder: Request.Builder = original.newBuilder()
                .header("Authorization", "Bearer $API_KEY")
                .header("Accept", "application/json")
            val new: Request = requestBuilder.build()
            chain.proceed(new)
        }
        // Log the response
        .addInterceptor(logging)
        .build()

    // Retrofit instance
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        // Convert json to kotlin objects
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Instance to interact with yelp api
    val yelpApiService: YelpApiService = retrofit.create(YelpApiService::class.java)
}