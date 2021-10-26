package com.whurtle.adulting.ui.main

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import org.koin.java.KoinJavaComponent

interface IMainInteractor {
    fun initialize(activity: AppCompatActivity, view: IMainView)
}

class MainInteractor : IMainInteractor {

    private var presenter: IMainPresenter
    private var router: IMainRouter

    init {
        var scope = KoinJavaComponent.getKoin().getScope(Scope.MAIN_MODULE_SCOPE.name)
        router = scope.get()
        presenter = scope.get()
    }

    override fun initialize(activity: AppCompatActivity, view: IMainView) {
        router.setWeakReference(activity)
        presenter.attach(view)
        router.loadDefaultFragment()
    }

}