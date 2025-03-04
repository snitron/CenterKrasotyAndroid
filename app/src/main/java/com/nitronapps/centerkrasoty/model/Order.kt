package com.nitronapps.centerkrasoty.model

import com.google.gson.annotations.SerializedName
import com.nitronapps.centerkrasoty.utils.trueAfter
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.internal.Intrinsics


data class Order(
    val date: String,
    val finishTime: String,
    val id: Int,
    val placeId: Int,
    val price: Double,
    @SerializedName("name") val serviceName: String,
    val startTime: String,
    val time: Long,
    @SerializedName("placeInfo") val placeName: String,
    val placeImage: String
): Comparable<Order> {

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
            SimpleDateFormat("dd.MM.yyyy", Locale("ru", "RU"))

        val parse =
            SimpleDateFormat("yyyy-MM-dd", Locale("ru", "RU")).parse(date)

        return formatter.format(parse!!).plus(" г.")
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

        return getStartTimeString() + " - " + getFinishTimeString()
    }

    fun checkIsFutureOrder(currentDate: Date): Boolean {
        val date = getStartTimeParsed()

        return date.trueAfter(currentDate)
    }

    override fun compareTo(other: Order): Int {
        val date = getStartTimeParsed()

        return when {
            date == other.getStartTimeParsed() -> 0
            date > other.getStartTimeParsed() -> 1
            else -> -1
        }
    }


}

data class OrderResponse(
    val code: Int,
    val count: Int,
    val orders: ArrayList<Order>
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

    fun getDay(): String {
        val date = Date()
        date.time = time

        val formatter =
            SimpleDateFormat("dd.MM.yyyy", Locale("ru", "RU"))

        return formatter.format(date).plus(" г.")
    }
}