package com.whurtle.adulting.store.grocery

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface GroceryDao {

    @Transaction
    @Query("SELECT * FROM grocery_item WHERE id = :id")
    fun getGroceryItemById(id: String): Single<GroceryItem>

    @Transaction
    @Query("SELECT * FROM grocery_item LIMIT :limit OFFSET :offset")
    fun getAllGroceryItems(limit: Int, offset: Int): Single<List<GroceryItem>>

    @Transaction
    @Query("SELECT * FROM grocery_item WHERE status = :status")
    fun getAllGroceryItemsWithStatus(status: String): Single<List<GroceryItem>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertGroceryItem(entry: GroceryEntry): Completable

    @Update
    fun updateGroceryItem(entry: GroceryEntry): Completable

    @Query("DELETE FROM grocery_item WHERE id = :id")
    fun deleteGroceryItemById(id: String): Completable

}