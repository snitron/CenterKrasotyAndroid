package com.nitronapps.centerkrasoty.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nitronapps.centerkrasoty.data.entity.Order
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable

@Dao
interface OrderDao {
    @Query("SELECT * FROM `order`")
    fun getAll(): Observable<Order>

    @Insert
    fun insert(vararg data: Order): Completable

    @Query("SELECT * FROM `order` WHERE id = :id")
    fun getById(id: Int): Maybe<Order>

    @Query("SELECT * FROM `order` WHERE serviceId = :id")
    fun getByServiceId(id: Int): Maybe<Order>
}