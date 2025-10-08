package com.example.tastebudge

// Placeholder class for a Player
data class User (
    val userName : String? = "",                // none of these two are implemented
    val userID : String = "",
    val votes : Map<String, String> = mapOf()   // restaurantID -> "vote"
)

