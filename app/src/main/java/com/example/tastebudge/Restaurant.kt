package com.example.tastebudge

data class Restaurant (
    val id: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val rating: Double = 0.0,
    val price: String = "",
    val distance: String = "",
    val address: String = "",
    val coordinates: Map<String, Double> = mapOf(),
    var score: Int = 0
)