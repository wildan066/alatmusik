    package com.example.toko.`model`

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

    @Parcelize
@Entity(tableName = "Toko_table")
data class Shop (
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val name: String,
    val price: String,
    val note:String,
    val latitude : Double?,
    val longitude : Double?

    ): Parcelable