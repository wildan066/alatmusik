package com.example.toko.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

import com.example.toko.model.Shop
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDao {
    @Query("SELECT * FROM Toko_table ORDER BY name ASC")
    fun getAllShop(): Flow<List<Shop>>

    @Insert
    suspend fun InsertShop(shop: Shop)

    @Delete
    suspend fun DeleteShop(shop: Shop)

    @Update fun UpdateShop(shop: Shop)


}