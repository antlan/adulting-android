package com.whurtle.adulting.store

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.AutoMigration
import com.whurtle.adulting.store.grocery.GroceryDao
import com.whurtle.adulting.store.grocery.GroceryEntry
import com.whurtle.adulting.store.inventory.InventoryDao
import com.whurtle.adulting.store.inventory.Item

@Database(
    entities = [Item::class, GroceryEntry::class],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
    ],
    exportSchema = true
)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun inventoryDao(): InventoryDao
    abstract fun groceryDao(): GroceryDao

    companion object {

        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getInstance(context: Context): AppRoomDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppRoomDatabase::class.java,
                "app.db"
            ).build()
    }

}