package com.nitronapps.centerkrasoty.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nitronapps.centerkrasoty.data.dao.OrderDao
import com.nitronapps.centerkrasoty.data.dao.UserInfoDao
import com.nitronapps.centerkrasoty.data.entity.Order
import com.nitronapps.centerkrasoty.data.entity.UserInfo

@Database(entities = arrayOf(UserInfo::class), version = 1, exportSchema = false)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): UserInfoDao
}
@Database(entities = arrayOf(Order::class), version = 1, exportSchema = false)
abstract class OrderDatabase: RoomDatabase() {
    abstract fun orderDao(): OrderDao
}