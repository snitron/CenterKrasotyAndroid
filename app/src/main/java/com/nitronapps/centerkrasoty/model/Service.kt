package com.nitronapps.centerkrasoty.model

data class ServiceResponse(
    val services: Array<Service>,
    val code: Int
)

data class Service(
    val id: Int, //TODO: Auto-install id in CMS
    val groupId: Int,
    val groupName: String,
    val name: String,
    val info: String,
    val price: Double
)