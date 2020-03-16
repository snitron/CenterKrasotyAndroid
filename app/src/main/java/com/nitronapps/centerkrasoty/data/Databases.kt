package com.nitronapps.centerkrasoty.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nitronapps.centerkrasoty.data.dao.OfficeDao
import com.nitronapps.centerkrasoty.data.dao.UserInfoDao
import com.nitronapps.centerkrasoty.data.entity.Office
import com.nitronapps.centerkrasoty.data.entity.UserInfo

@Database(entities = arrayOf(UserInfo::class), version = 2, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserInfoDao
}

@Database(entities = arrayOf(Office::class), version = 3, exportSchema = false)
abstract class OfficeDatabase : RoomDatabase() {
    abstract fun officeDao(): OfficeDao
}