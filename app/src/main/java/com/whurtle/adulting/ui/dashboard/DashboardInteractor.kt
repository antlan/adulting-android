package com.whurtle.adulting.ui.dashboard

import androidx.fragment.app.Fragment
import org.koin.java.KoinJavaComponent

interface IDashboardInteractor {
    fun initialize(fragment: Fragment, view: IDashboardView)
    fun onInventoryButtonClicked()
    fun onGroceryButtonClicked()
}

class DashboardInteractor : IDashboardInteractor {

    private var router: IDashboardRouter
    private var presenter: IDashboardPresenter

    init {
        var scope = KoinJavaComponent.getKoin().getScope(Scope.DASHBOARD_MODULE_SCOPE.name)
        router = scope.get()
        presenter = scope.get()
    }

    override fun initialize(fragment: Fragment, view: IDashboardView) {
        router.setWeakReference(fragment)
        presenter.setView(view)
    }

    override fun onInventoryButtonClicked() {
        router.gotoInventoryView()
    }

    override fun onGroceryButtonClicked() {
        router.gotoGroceryView()
    }

}