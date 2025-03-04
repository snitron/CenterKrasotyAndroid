package com.nitronapps.centerkrasoty.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserInfo(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo val token: String
)