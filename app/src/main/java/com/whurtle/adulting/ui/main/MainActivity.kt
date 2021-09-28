package com.whurtle.adulting.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.whurtle.adulting.R
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.scope
import org.koin.core.qualifier.named

interface IMainView {
}


class MainActivity : IMainView, AppCompatActivity() {

    val presenter: IMainPresenter
    val router: IMainRouter
    val interactor: IMainInteractor

    init {
        var scope = getKoin().createScope(SCOPE, named(SCOPE))
        presenter = scope.get()
        router = scope.get()
        interactor = scope.get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        router.setWeakReference(this)
        setContentView(R.layout.activity_main)
    }

    override fun onDestroy() {
        super.onDestroy()
        getKoin().deleteScope(SCOPE)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.attach(this)
        interactor.initialize()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.detach()
    }
}
