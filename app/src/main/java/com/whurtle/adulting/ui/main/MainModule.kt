package com.whurtle.adulting.ui.main

import org.koin.core.qualifier.named
import org.koin.dsl.module

enum class Scope {
    MAIN_MODULE_SCOPE
}

val mainModule = module {

    //scoped router hehe
    scope(named(Scope.MAIN_MODULE_SCOPE.name)) {
        scoped { MainRouter() as IMainRouter }
        scoped { MainPresenter() as IMainPresenter }
        scoped { MainInteractor() as IMainInteractor }
    }
}