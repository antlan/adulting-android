package com.whurtle.adulting.ui.inventory.item.view

interface IViewItemPresenter {

    fun setView(view: IViewItemView)

    fun showMessage(message: String)

    fun showError(message: String)
}

class ViewItemPresenter : IViewItemPresenter {

    private lateinit var view: IViewItemView

    override fun setView(view: IViewItemView) {
        this.view = view
    }

    override fun showMessage(message: String) {
        //TODO("Not yet implemented")

    }

    override fun showError(message: String) {
        //TODO("Not yet implemented")
    }

}