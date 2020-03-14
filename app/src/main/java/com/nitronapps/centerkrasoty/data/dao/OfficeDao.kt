package com.nitronapps.centerkrasoty.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nitronapps.centerkrasoty.data.entity.Office
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable

@Dao
interface OfficeDao {
    @Insert
    fun insert(vararg office: Office): Completable

    @Query("SELECT * FROM office")
    fun getAll(): Observable<Office>

    @Query("SELECT * FROM office WHERE city = :city")
    fun getByCity(city: String): Maybe<Office>

    @Query("DELETE FROM office")
    fun deleteAll(): Completable

    @Query("SELECT COUNT(id) FROM office")
    fun getRowCount(): Int
}