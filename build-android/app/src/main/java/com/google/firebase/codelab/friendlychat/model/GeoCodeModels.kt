package com.google.firebase.codelab.friendlychat.model

data class AddressComponent(
    val longName: String,
    val shortName: String,
    val types: List<String>
)

data class Location(
    val lat: Double,
    val lng: Double
)

data class Viewport(
    val northeast: Location,
    val southwest: Location
)

data class Geometry(
    val location: Location,
    val locationType: String,
    val viewport: Viewport
)

data class PlusCode(
    val compoundCode: String,
    val globalCode: String
)

data class Result(
    val address_components: List<AddressComponent>,
    val formatted_address: String,
    val geometry: Geometry,
    val placeId: String,
    val plusCode: PlusCode,
    val types: List<String>
)

data class GeocodeResponse(
    val results: List<Result>,
    val status: String
)