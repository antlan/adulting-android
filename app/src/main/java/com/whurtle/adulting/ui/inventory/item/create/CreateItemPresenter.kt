package com.whurtle.adulting.ui.inventory.item.create

import com.whurtle.adulting.ui.dashboard.IDashboardView

interface ICreateItemPresenter {
    fun setView(view: ICreateItemView)
    fun showMessage(message: String)
    fun showError(message: String)
}

class CreateItemPresenter : ICreateItemPresenter {

    private lateinit var view: ICreateItemView

    override fun setView(view: ICreateItemView) {
        this.view = view
    }

    override fun showMessage(message: String) {
        view.showMessage(message)
    }

    override fun showError(message: String) {
        view.showMessage(message)
    }

}