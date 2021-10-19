package com.whurtle.adulting.store.inventory

import com.whurtle.adulting.store.AppRoomDatabase
import io.reactivex.Completable
import io.reactivex.Single
import org.koin.java.KoinJavaComponent.getKoin

interface IInventoryStore {

    fun createItem(item: Item): Completable
    fun getItem(id: String): Single<Item>
    fun deleteItem(id: String): Completable
    fun updateItem(item: Item): Completable
    fun getAllItems(limit: Int, offset: Int): Single<List<Item>>
}

class InventoryStore : IInventoryStore {
    private val database: AppRoomDatabase = getKoin().get()

    var items: HashMap<String, Item> = HashMap()

    override fun createItem(item: Item): Completable {
        return database.inventoryDao().insertItem(item)
    }

    override fun getItem(id: String): Single<Item> {
        return database.inventoryDao().getItemById(id)
    }

    override fun deleteItem(id: String): Completable {
        return database.inventoryDao().deleteItemById(id)
    }

    override fun updateItem(item: Item): Completable {
        return database.inventoryDao().updateItem(item)
    }

    override fun getAllItems(limit: Int, offset: Int): Single<List<Item>> {
        return database.inventoryDao().getAllItems(limit, offset)
    }
}