package com.nitronapps.centerkrasoty.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Office(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val info: String,
    @ColumnInfo val address: String,
    @ColumnInfo val city: String,
    @ColumnInfo val geoCoords: String
)