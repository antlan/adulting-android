package com.whurtle.adulting.ui.main

import org.koin.java.KoinJavaComponent

interface IMainInteractor {
    fun initialize()
}

class MainInteractor : IMainInteractor {

    private var router: IMainRouter

    init {
        var scope = KoinJavaComponent.getKoin().getScope(SCOPE)
        router = scope.get()
    }

    override fun initialize() {
        router.loadDefaultFragment()
    }

}