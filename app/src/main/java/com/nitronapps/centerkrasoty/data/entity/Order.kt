package com.nitronapps.centerkrasoty.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Order(@PrimaryKey val id: Int,
                 @ColumnInfo val officeId: Int,
                 @ColumnInfo val officeName: String,
                 @ColumnInfo val serviceId: Int,
                 @ColumnInfo val serviceName: Int,
                 @ColumnInfo val placeId: Int,
                 @ColumnInfo val placeName: Int,
                 @ColumnInfo val date: String,
                 @ColumnInfo val price: Double,
                 @ColumnInfo val isNofiticated: Boolean)