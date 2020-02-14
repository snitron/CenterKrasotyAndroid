package com.nitronapps.centerkrasoty.model


data class AuthRequest(val login: String,
                       val password: String,
                       val phone: String)

data class AuthResponse(val login: String,
                        val token: String,
                        val code: Int)