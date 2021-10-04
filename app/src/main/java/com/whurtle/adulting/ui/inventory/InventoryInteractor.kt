package com.whurtle.adulting.ui.inventory

import org.koin.java.KoinJavaComponent

interface IInventoryInteractor {
    fun onCreateItemButtonClicked()
}

class InventoryInteractor : IInventoryInteractor {

    private var router: IInventoryRouter

    init {
        var scope = KoinJavaComponent.getKoin().getScope(Scope.INVENTORY_MODULE_SCOPE.name)
        router = scope.get()
    }

    override fun onCreateItemButtonClicked() {
        router.gotoCreateItemView()
    }
}