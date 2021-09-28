package com.whurtle.adulting.ui.dashboard

import org.koin.java.KoinJavaComponent

interface IDashboardInteractor {
    fun initialize()
    fun onInventoryButtonClicked()
}

class DashboardInteractor : IDashboardInteractor {

    private var router: IDashboardRouter

    init {
        var scope = KoinJavaComponent.getKoin().getScope(Scope.DASHBOARD_MODULE_SCOPE.name)
        router = scope.get()
    }

    override fun initialize() {
    }

    override fun onInventoryButtonClicked() {
        router.gotoInventoryView()
    }

}