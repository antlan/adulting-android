package com.whurtle.adulting.store.inventory

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface InventoryDao {

    @Query("SELECT * FROM Item WHERE id = :id")
    fun getItemById(id: String): Single<Item>

    @Query("SELECT * FROM Item LIMIT :limit OFFSET :offset")
    fun getAllItems(limit: Int, offset: Int): Single<List<Item>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertItem(item: Item): Completable

    @Update
    fun updateItem(item: Item): Completable

    @Query("DELETE FROM Item WHERE id = :id")
    fun deleteItemById(id: String): Completable

}