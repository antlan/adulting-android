package com.whurtle.adulting.ui.grocery

import com.whurtle.adulting.store.grocery.GroceryItem
import com.whurtle.adulting.store.grocery.GroceryItemFull
import com.whurtle.adulting.store.inventory.Item

interface IGroceryPresenter {
    fun setView(view: IGroceryView)
    fun setInventoryListItems(items: List<GroceryItemFull>)
    fun clearListItems()
}

class GroceryPresenter : IGroceryPresenter {

    private lateinit var view: IGroceryView

    override fun setView(view: IGroceryView) {
        this.view = view
    }


    override fun setInventoryListItems(items: List<GroceryItemFull>) {
        view.setInventoryListItems(items)
    }

    override fun clearListItems() {
        view.clearListItems()
    }
}