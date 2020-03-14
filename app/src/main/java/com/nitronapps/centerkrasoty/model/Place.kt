package com.nitronapps.centerkrasoty.model

data class PlaceResponse(
    val code: Int,
    val places: Array<Place>
)

class Place(
    val id: Int,
    val info: String
)