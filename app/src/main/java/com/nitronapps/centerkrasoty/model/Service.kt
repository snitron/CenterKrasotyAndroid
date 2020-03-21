package com.nitronapps.centerkrasoty.model

data class ServiceResponse(
    val services: ArrayList<Service>,
    val code: Int
)

data class Service(
    val id: Int, //TODO: Auto-install id in CMS
    val groupId: Int,
    val groupName: String,
    val name: String,
    val info: String,
    val price: Double,
    val long: Long
): Comparable<Service> {

    override fun compareTo(other: Service): Int {
        return when {
            price == other.price -> 0
            price > other.price -> 1
            else -> -1
        }
    }

}