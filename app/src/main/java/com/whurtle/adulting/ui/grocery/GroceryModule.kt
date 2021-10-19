package com.whurtle.adulting.ui.grocery

import org.koin.core.qualifier.named
import org.koin.dsl.module

enum class Scope {
    INVENTORY_MODULE_SCOPE
}

val groceryModule = module {

    //scoped router hehe
    scope(named(Scope.INVENTORY_MODULE_SCOPE.name)) {
        scoped<IGroceryRouter> { GroceryRouter() }
        scoped<IGroceryPresenter> { GroceryPresenter() }
        scoped<IGroceryInteractor> { GroceryInteractor() }
    }
}