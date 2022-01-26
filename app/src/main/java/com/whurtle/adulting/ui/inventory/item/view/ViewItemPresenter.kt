package com.whurtle.adulting.ui.inventory.item.view

import com.whurtle.adulting.store.inventory.Item

interface IViewItemPresenter {

    fun setView(view: IViewItemView)

    fun showMessage(message: String)

    fun showError(message: String)
    fun populateView(item: Item?)
}

class ViewItemPresenter : IViewItemPresenter {

    private lateinit var view: IViewItemView

    override fun setView(view: IViewItemView) {
        this.view = view
    }

    override fun showMessage(message: String) {
        view.showMessage(message)
    }

    override fun showError(message: String) {
        view.showMessage(message)
    }

    override fun populateView(item: Item?) {
        view.populateView(item)
    }
}