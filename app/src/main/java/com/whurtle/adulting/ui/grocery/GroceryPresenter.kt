package com.whurtle.adulting.ui.grocery

import com.whurtle.adulting.store.grocery.GroceryItem

interface IGroceryPresenter {
    fun setView(view: IGroceryView)
    fun setInventoryListItems(items: List<GroceryItem>)
    fun clearListItems()
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
}