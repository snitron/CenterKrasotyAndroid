package com.nitronapps.centerkrasoty.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.jvm.internal.Intrinsics

fun Date.getPlused(time: Long): Date {
    val copy = Date(this.time)
    copy.time = copy.time + time
    return copy
}

fun Date.trueBefore(date: Date): Boolean {
    return this.time < date.time
}

fun Date.trueAfter(date: Date): Boolean {
    return this.time > date.time
}


fun String.withFirstUpperLetter(): String {

    val sb = StringBuilder()
    val valueOf = this[0].toString()
    val locale = Locale("ru", "RU")
    val upperCase = valueOf.toUpperCase(locale)

    sb.append(upperCase)
    val substring = this.substring(1)

    sb.append(substring)
    return sb.toString()
}

inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(json, object: TypeToken<T>() {}.type)