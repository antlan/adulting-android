package com.whurtle.adulting.store

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import com.whurtle.adulting.store.grocery.GroceryDao
import com.whurtle.adulting.store.grocery.GroceryEntry
import com.whurtle.adulting.store.inventory.InventoryDao
import com.whurtle.adulting.store.inventory.Item

@Database(
    entities = [Item::class, GroceryEntry::class],
    version = 4,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4, spec = AppRoomDatabase.Migration_3_to_4::class),
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

    @DeleteColumn(tableName = "item", columnName = "is_perishable")
    class Migration_3_to_4 : AutoMigrationSpec


}