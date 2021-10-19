package com.whurtle.adulting.ui.inventory

import com.whurtle.adulting.store.inventory.Item

interface IInventoryPresenter {
    fun setView(view: IInventoryView)
    fun showMessage(message: String)
    fun setInventoryListItems(items: List<Item>)
    fun clearListItems()
}

class InventoryPresenter : IInventoryPresenter {

    private lateinit var view: IInventoryView

    override fun setView(view: IInventoryView) {
        this.view = view
    }

    override fun showMessage(message: String) {
        view.showMessage(message)
    }


    override fun setInventoryListItems(items: List<Item>) {
        view.setInventoryListItems(items)
    }

    override fun clearListItems() {
        view.clearListItems()
    }
}