package com.nitronapps.centerkrasoty.model

data class SMSRequest(val login: String,
                      val code: Int)

data class SMSResponse(val login: String,
                       val result: Boolean,
                       val token: String)