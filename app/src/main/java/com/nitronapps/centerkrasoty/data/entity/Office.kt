package com.nitronapps.centerkrasoty.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.internal.Intrinsics


@Entity
data class Office(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val info: String,
    @ColumnInfo val address: String,
    @ColumnInfo val city: String,
    @ColumnInfo val geoCoords: String,
    @ColumnInfo val startTime: String,
    @ColumnInfo val finishTime: String
) {
    @Ignore
    private val formatter =
        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale("ru", "RU"))

    fun getStartTimeParsed(date: String): Date {
        val sb = StringBuilder()
        sb.append(date)
        sb.append(" ")
        sb.append(startTime)

        return formatter.parse(sb.toString())!!
    }

    fun getFinishTimeParsed(date: String): Date {
        val sb = StringBuilder()
        sb.append(date)
        sb.append(" ")
        sb.append(finishTime)

        return formatter.parse(sb.toString())!!
    }
}