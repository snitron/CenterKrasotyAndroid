package com.nitronapps.centerkrasoty.model

data class TransactionRequest(
    val date: String,
    val startTime: String,
    val finishTime: String,
    val groupId: Int,
    val placeId: Int
)

data class TransactionResponse(
    val code: Int
)