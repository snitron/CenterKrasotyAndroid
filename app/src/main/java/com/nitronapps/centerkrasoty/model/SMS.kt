package com.nitronapps.centerkrasoty.model

import com.google.gson.annotations.SerializedName

data class SMSRequest(
    val login: String,
    val code: Int
)

data class SMSResponse(
    @SerializedName("login") val login: String,
    @SerializedName("code") val code: Int
)