package com.nitronapps.centerkrasoty.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nitronapps.centerkrasoty.data.entity.UserInfo
import androidx.lifecycle.LiveData



@Dao
interface UserInfoDao{
    @Query("SELECT * from userinfo")
    fun getAll(): List<UserInfo>

    @Insert
    fun add(data: UserInfo)

    @Delete
    fun delete(data: UserInfo)

    @Query("SELECT COUNT(id) FROM userinfo")
    fun getRowCount(): Int
}