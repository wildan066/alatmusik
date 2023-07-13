package com.example.toko.Application

import android.app.Application
import com.example.toko.Repository.ShopRepository

class ShopApplication: Application() {
    val database by lazy { ShopDatabase.getDatabase( this) }
    val repository by lazy { ShopRepository(database.shopDao()) }
}