package com.nitronapps.centerkrasoty.model

import com.nitronapps.centerkrasoty.data.entity.Office

data class OfficeResponseObject(
    val id: Int,
    val name: String,
    val info: String,
    val city: String,
    val address: String,
    val geoCoordinates: String,
    val startTime: String,
    val finishTime: String
) {
    fun convertToOfficeDB(): Office {
        return Office(
            id = id,
            name = name,
            info = info,
            address = address,
            city = city,
            geoCoords = geoCoordinates,
            startTime = startTime,
            finishTime = finishTime
        )
    }
}

data class OfficeResponse(
    val offices: ArrayList<OfficeResponseObject>,
    val code: Int
)