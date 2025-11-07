package com.example.tastebudge

import android.util.Log

object RestaurantManager {
    private val yelpService = RetrofitClient.yelpApiService

    // Search restaurant by location
    suspend fun searchRestaurants(
        latitude: Double?,
        longitude: Double?,
    ): List<Restaurant>? {
        if (latitude == null || longitude == null) return null
        return try {
            val response = yelpService.searchRestaurants(
                latitude = latitude,
                longitude = longitude,
                limit = 20
            )

            response.businesses.map { it.toAppRestaurant() }
        } catch (e: Exception) {
            Log.e("RestaurantManager", "Error fetching restaurants: ${e.message}")
            null
        }
    }

    // Search restaurant by name
    suspend fun searchRestaurantsByTerm(
        term: String
    ): List<Restaurant>? {
        return try {
            val response = yelpService.searchRestaurantsByTerm(
                term = term,
                limit = 20
            )

            response.businesses.map { it.toAppRestaurant() }
        } catch (e: Exception) {
            Log.e("RestaurantManager", "Error fetching restaurants: ${e.message}")
            null
        }
    }

    // Extension function to convert Yelp business to our app's Restaurant model
    private fun YelpBusiness.toAppRestaurant(): Restaurant {
        return Restaurant(
            id = this.id,
            name = this.name,
            imageUrl = this.image_url,
            rating = this.rating,
            price = this.price ?: "$$", // Default if null
            distance = this.distance.toString(),
            //address = this.location.display_address.joinToString(", "),
            coordinates = mapOf(
                "lat" to this.coordinates.latitude,
                "lng" to this.coordinates.longitude
            ),
            score = 0
        )
    }
}