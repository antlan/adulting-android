package com.whurtle.adulting.ui.inventory

import org.koin.core.qualifier.named
import org.koin.dsl.module

enum class Scope {
    INVENTORY_MODULE_SCOPE
}

val inventoryModule = module {

    //scoped router hehe
    scope(named(Scope.INVENTORY_MODULE_SCOPE.name)) {
        scoped { InventoryRouter() }
        scoped { InventoryPresenter() }
        scoped { InventoryInteractor() }
    }
}