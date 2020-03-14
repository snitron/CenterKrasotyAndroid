package com.nitronapps.centerkrasoty.model

import com.nitronapps.centerkrasoty.data.entity.Office

data class OfficeResponseObject(
    val id: Int,
    val name: String,
    val info: String,
    val city: String,
    val address: String,
    val geoCoordinates: String,
    val startTime: String
) {
    fun convertToOfficeDB(): Office {
        return Office(
            id,
            name,
            info,
            address,
            city,
            geoCoordinates
        )
    }
}

data class OfficeResponse(
    val offices: Array<OfficeResponseObject>,
    val code: Int
)