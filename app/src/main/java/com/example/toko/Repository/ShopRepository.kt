package com.example.toko.Repository

import com.example.toko.Dao.ShopDao
import com.example.toko.model.Shop
import kotlinx.coroutines.flow.Flow

class ShopRepository(private val  shopDao: ShopDao) {
    val allshop: Flow<List<Shop>> = shopDao.getAllShop()

    suspend fun InsertShop(shop: Shop){
        shopDao.InsertShop(shop)
    }
    suspend fun DeleteShop(shop: Shop){
        shopDao.DeleteShop(shop)
    }
    suspend fun UpdateShop(shop: Shop){
        shopDao.UpdateShop(shop)
    }
}