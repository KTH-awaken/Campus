package com.google.firebase.codelab.friendlychat.model

data class Northeast(
    val lat: Double,
    val lng: Double
)

data class Southwest(
    val lat: Double,
    val lng: Double
)

data class Bounds(
    val northeast: Northeast,
    val southwest: Southwest
)

data class Location(
    val lat: Double,
    val lng: Double
)

data class Viewport(
    val northeast: Northeast,
    val southwest: Southwest
)

data class Geometry(
    val bounds: Bounds,
    val location: Location,
    val location_type: String,
    val viewport: Viewport
)

data class AddressComponent(
    val longName: String,
    val shortName: String,
    val types: List<String>
)

data class Result(
    val address_components: List<AddressComponent>,
    val formatted_address: String,
    val geometry: Geometry,
    val placeId: String,
    val types: List<String>
)

data class GeocodeResponse(
    val results: List<Result>,
    val status: String
)