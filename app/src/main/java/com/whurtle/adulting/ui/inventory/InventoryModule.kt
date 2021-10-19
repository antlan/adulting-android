package com.whurtle.adulting.ui.inventory

import org.koin.core.qualifier.named
import org.koin.dsl.module

enum class Scope {
    INVENTORY_MODULE_SCOPE
}

val inventoryModule = module {

    //scoped router hehe
    scope(named(Scope.INVENTORY_MODULE_SCOPE.name)) {
        scoped<IInventoryRouter> { InventoryRouter() }
        scoped<IInventoryPresenter> { InventoryPresenter() }
        scoped<IInventoryInteractor> { InventoryInteractor() }
    }
}