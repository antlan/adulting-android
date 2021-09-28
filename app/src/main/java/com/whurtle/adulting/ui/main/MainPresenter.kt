package com.whurtle.adulting.ui.main

import org.koin.core.scope.get
import org.koin.java.KoinJavaComponent.getKoin
import org.koin.java.KoinJavaComponent.inject


interface IMainPresenter {
    fun attach(view: IMainView)
    fun detach()
}

class MainPresenter : IMainPresenter {

    private var view: IMainView? = null

    override fun attach(view: IMainView) {
        this.view = view
    }

    override fun detach() {
        this.view = null
    }


}