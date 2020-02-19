package com.nitronapps.centerkrasoty.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nitronapps.centerkrasoty.data.entity.UserInfo
import androidx.lifecycle.LiveData
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable


@Dao
interface UserInfoDao{
    @Query("SELECT * from userinfo")
    fun getAll(): Observable<List<UserInfo>>

    @Insert
    fun insert(data: UserInfo): Completable

    @Delete
    fun delete(data: UserInfo): Completable

    @Query("SELECT * FROM userinfo WHERE id = :id")
    fun getById(id: Int): Maybe<UserInfo>

    @Query("SELECT COUNT(id) FROM userinfo")
    fun getRowCount(): Int
}