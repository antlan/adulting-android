package com.whurtle.adulting.ui.dashboard

interface IDashboardPresenter {
    fun setView(view: IDashboardView)
}

class DashboardPresenter : IDashboardPresenter {
    private lateinit var view: IDashboardView

    override fun setView(view: IDashboardView) {
        this.view = view
    }

}