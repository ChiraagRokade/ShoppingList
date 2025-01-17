package com.example.shoppinglist.models

data class LocationData(
    var latitude: Double,
    var longitude: Double,
)

data class GeocodingResponse(
    var results: List<GeocodingResult>,
    var status: String
)

data class GeocodingResult (
        var formatted_address: String,
)
