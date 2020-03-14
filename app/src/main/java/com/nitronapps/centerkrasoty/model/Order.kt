package com.nitronapps.centerkrasoty.model

import com.google.gson.annotations.SerializedName
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.internal.Intrinsics


data class Order(
    val date: String,
    val finishTime: String,
    val id: Int,
    val login: String,
    val placeId: Int,
    val price: Double,
    @SerializedName("name") val serviceName: String,
    val startTime: String,
    val time: Long
) {

    fun getStartTimeParsed(): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())


        return formatter.parse(date + " " + startTime)!!
    }

    fun getFinishTimeParsed(): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())


        return formatter.parse(date + " " + finishTime)!!
    }

    fun getDay(): String {
        val formatter =
            SimpleDateFormat("dd MM yyyy", Locale("ru", "RU"))

        val parse =
            SimpleDateFormat("yyyy-MM-dd", Locale("ru", "RU")).parse(date)

        return formatter.format(parse!!)
    }

    fun getStartTimeString(): String {
        val format =
            SimpleDateFormat("HH:mm", Locale("ru", "RU")).format(getStartTimeParsed())

        return format
    }

    fun getFinishTimeString(): String {
        val format =
            SimpleDateFormat("HH:mm", Locale("ru", "RU")).format(getFinishTimeParsed())

        return format
    }

    fun getTimeRange(): String {

        return getStartTimeString() + " - " + getFinishTimeParsed()
    }
}

data class OrderResponse(
    val code: Int,
    val count: Int,
    val orders: Array<Order>
)

class PreOrder(
    val place: Place,
    val service: Service,
    val time: Long
) {

    fun getDurationPretty(): String {
        return getStartDate() + " - " + getFinishTime()
    }

    fun getStartDate(): String {
        val formatter = SimpleDateFormat("HH:mm", Locale("ru", "RU"))

        val date = Date()
        date.time = time

        return formatter.format(date)
    }

    fun getFinishTime(): String {
        val formatter = SimpleDateFormat("HH:mm", Locale("ru", "RU"))

        val date = Date()
        date.time = time + service.long * 60000

        return formatter.format(date)
    }

    fun getDate(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale("ru", "RU"))

        val date = Date()
        date.time = time

        return formatter.format(date)
    }
}