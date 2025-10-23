package com.example.tastebudge

import retrofit2.http.GET
import retrofit2.http.Query

// Declare methods and queries
interface YelpApiService {
    @GET("businesses/search")
    suspend fun searchRestaurants(
        @Query("term") term: String = "restaurants",
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Int = 4000,
        @Query("limit") limit: Int = 20,
        @Query("sort_by") sortBy: String = "distance"
    ): YelpSearchResponse

    @GET("businesses/search")
    suspend fun searchRestaurantsByTerm(
        @Query("location") location: String = "Hong Kong",
        @Query("term") term: String,
        @Query("radius") radius: Int = 4000,
        @Query("limit") limit: Int = 20
    ): YelpSearchResponse
}

