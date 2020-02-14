package com.nitronapps.centerkrasoty.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nitronapps.centerkrasoty.data.entity.Order

@Dao
interface OrderDao {
    @Query("SELECT * FROM `order`")
    fun getAll(): List<Order>

    @Insert
    fun addAll(vararg data: Order)

    @Query("SELECT * FROM `order` WHERE id = :id")
    fun getById(id: Int): Order

    @Query("SELECT * FROM `order` WHERE serviceId = :id")
    fun getByServiceId(id: Int): Order
}