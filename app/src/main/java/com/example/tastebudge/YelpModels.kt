package com.example.tastebudge

// Main search response
data class YelpSearchResponse(
    val businesses: List<YelpBusiness>,
    val total: Int,
    val region: YelpRegion
)

// Individual business
data class YelpBusiness(
    val id: String,
    val name: String,
    val imageUrl: String,
    val url: String,
    val rating: Double,
    val price: String?,
    val phone: String,
    val displayPhone: String,
    val reviewCount: Int,
    val categories: List<YelpCategory>,
    val location: YelpLocation,
    val coordinates: YelpCoordinates,
    val distance: Double
)

// Supporting classes
data class YelpCategory(
    val alias: String,
    val title: String
)

data class YelpLocation(
    val address1: String?,
    val address2: String?,
    val address3: String?,
    val city: String,
    val zipCode: String,
    val country: String,
    val state: String,
    val displayAddress: List<String>
)

data class YelpCoordinates(
    val latitude: Double,
    val longitude: Double
)

data class YelpRegion(
    val center: YelpCoordinates
)