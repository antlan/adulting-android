package com.whurtle.adulting.store.grocery

import com.whurtle.adulting.store.AppRoomDatabase
import io.reactivex.Completable
import io.reactivex.Single
import org.koin.java.KoinJavaComponent.getKoin

interface IGroceryStore {

    fun createItem(item: GroceryItem): Completable
    fun getItem(id: String): Single<GroceryItemFull>
    fun deleteItem(id: String): Completable
    fun updateItem(item: GroceryItem): Completable
    fun getAllItems(limit: Int, offset: Int): Single<List<GroceryItemFull>>
}

class GroceryStore : IGroceryStore {
    private val database: AppRoomDatabase = getKoin().get()

    var items: HashMap<String, GroceryItem> = HashMap()

    override fun createItem(item: GroceryItem): Completable {
        return database.groceryDao().insertGroceryItem(item)
    }

    override fun getItem(id: String): Single<GroceryItemFull> {
        return database.groceryDao().getGroceryItemById(id)
    }

    override fun deleteItem(id: String): Completable {
        return database.groceryDao().deleteGroceryItemById(id)
    }

    override fun updateItem(item: GroceryItem): Completable {
        return database.groceryDao().updateGroceryItem(item)
    }

    override fun getAllItems(limit: Int, offset: Int): Single<List<GroceryItemFull>> {
        return database.groceryDao().getAllGroceryItems(limit, offset)
    }
}