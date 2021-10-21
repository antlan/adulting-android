package com.whurtle.adulting.store.grocery

import com.whurtle.adulting.store.AppRoomDatabase
import io.reactivex.Completable
import io.reactivex.Single
import org.koin.java.KoinJavaComponent.getKoin

interface IGroceryStore {

    fun createItem(entry: GroceryEntry): Completable
    fun getItem(id: String): Single<GroceryItem>
    fun deleteItem(id: String): Completable
    fun updateItem(entry: GroceryEntry): Completable
    fun getAllItems(limit: Int, offset: Int): Single<List<GroceryItem>>
}

class GroceryStore : IGroceryStore {
    private val database: AppRoomDatabase = getKoin().get()

    var items: HashMap<String, GroceryEntry> = HashMap()

    override fun createItem(entry: GroceryEntry): Completable {
        return database.groceryDao().insertGroceryItem(entry)
    }

    override fun getItem(id: String): Single<GroceryItem> {
        return database.groceryDao().getGroceryItemById(id)
    }

    override fun deleteItem(id: String): Completable {
        return database.groceryDao().deleteGroceryItemById(id)
    }

    override fun updateItem(entry: GroceryEntry): Completable {
        return database.groceryDao().updateGroceryItem(entry)
    }

    override fun getAllItems(limit: Int, offset: Int): Single<List<GroceryItem>> {
        return database.groceryDao().getAllGroceryItems(limit, offset)
    }
}