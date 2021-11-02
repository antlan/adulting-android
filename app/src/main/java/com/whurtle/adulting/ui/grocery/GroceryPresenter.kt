package com.whurtle.adulting.ui.grocery

import com.whurtle.adulting.store.grocery.GroceryEntry
import com.whurtle.adulting.store.grocery.GroceryItem

interface IGroceryPresenter {
    fun setView(view: IGroceryView)
    fun setInventoryListItems(items: List<GroceryItem>)
    fun clearListItems()
    fun showUpdateOptions(item: GroceryItem)
    fun refreshItemDuetoUpdate(groceryEntry: GroceryEntry)
    fun refreshItemDuetoDelete(groceryEntry: GroceryEntry)
    fun doneProcessingCompleted()
}

class GroceryPresenter : IGroceryPresenter {

    private lateinit var view: IGroceryView

    override fun setView(view: IGroceryView) {
        this.view = view
    }


    override fun setInventoryListItems(items: List<GroceryItem>) {
        view.setInventoryListItems(items)
    }

    override fun clearListItems() {
        view.clearListItems()
    }

    override fun showUpdateOptions(item: GroceryItem) {
        view.showUpdateOptions(item)
    }

    override fun refreshItemDuetoUpdate(groceryEntry: GroceryEntry) {
        view.updateGroceryItem(groceryEntry)
    }

    override fun refreshItemDuetoDelete(groceryEntry: GroceryEntry) {
        view.deleteGroceryItem(groceryEntry)
    }

    override fun doneProcessingCompleted() {
        view.showProcessingComplete()
    }
}