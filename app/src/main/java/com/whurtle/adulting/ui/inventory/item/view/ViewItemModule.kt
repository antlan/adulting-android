package com.whurtle.adulting.ui.inventory.item.view

import org.koin.core.qualifier.named
import org.koin.dsl.module

enum class Scope {
    VIEW_ITEM_MODULE_SCOPE
}

val viewItemModule = module {

    //scoped router hehe
    scope(named(Scope.VIEW_ITEM_MODULE_SCOPE.name)) {
        scoped<IViewItemRouter> { ViewItemRouter() }
        scoped<IViewItemPresenter> { ViewItemPresenter() }
        scoped<IViewItemInteractor> { ViewItemInteractor() }
    }
}