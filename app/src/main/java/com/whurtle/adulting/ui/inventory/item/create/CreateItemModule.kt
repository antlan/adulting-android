package com.whurtle.adulting.ui.inventory.item.create

import org.koin.core.qualifier.named
import org.koin.dsl.module

enum class Scope {
    CREATE_ITEM_MODULE_SCOPE
}

val createItemModule = module {

    //scoped router hehe
    scope(named(Scope.CREATE_ITEM_MODULE_SCOPE.name)) {
        scoped { CreateItemRouter() as ICreateItemRouter }
        scoped { CreateItemPresenter() as ICreateItemPresenter }
        scoped { CreateItemInteractor() as ICreateItemInteractor }
    }
}