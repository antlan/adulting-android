package com.whurtle.adulting.store.grocery

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface GroceryDao {

    @Transaction
    @Query("SELECT * FROM grocery_item WHERE id = :id")
    fun getGroceryItemById(id: String): Single<GroceryItemFull>

    @Transaction
    @Query("SELECT * FROM grocery_item LIMIT :limit OFFSET :offset")
    fun getAllGroceryItems(limit: Int, offset: Int): Single<List<GroceryItemFull>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertGroceryItem(item: GroceryItem): Completable

    @Update
    fun updateGroceryItem(item: GroceryItem): Completable

    @Query("DELETE FROM grocery_item WHERE id = :id")
    fun deleteGroceryItemById(id: String): Completable

}