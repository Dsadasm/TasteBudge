package com.example.tastebudge

import java.util.UUID

// Placeholder class for a Player
data class User (
    // Create a globally unique UUID, so no two user instances are ever the same
    val userID : String = UUID.randomUUID().toString(),
    val votes : Map<String, String> = mapOf()   // restaurantID -> "vote"
)

