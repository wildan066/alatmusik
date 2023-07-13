package com.example.toko.Application

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.toko.Dao.ShopDao
import com.example.toko.model.Shop


@Database(entities = [Shop::class], version = 2, exportSchema = false)
@Entity
abstract class  ShopDatabase: RoomDatabase(){
    abstract fun shopDao():ShopDao

    companion object{
        private var INSTANCE : ShopDatabase?=null
        private val migration1to2:Migration=object:Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Toko_table ADD COLUMN latitude Double DEFAULT 0.0")
                database.execSQL("ALTER TABLE Toko_table ADD COLUMN longitude Double DEFAULT 0.0")
            }

        }

        fun getDatabase(context: Context): ShopDatabase{
            return INSTANCE ?: synchronized(this){
                val instance= Room.databaseBuilder(
                 context.applicationContext,
                 ShopDatabase::class.java,
                    "Shop-Database_1"
                )
                    .addMigrations(migration1to2)
                    .allowMainThreadQueries()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}