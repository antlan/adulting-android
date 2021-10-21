package com.whurtle.adulting.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.whurtle.adulting.R
import org.koin.android.ext.android.getKoin
import org.koin.core.qualifier.named

interface IMainView {
}


class MainActivity : IMainView, AppCompatActivity() {

    private val interactor: IMainInteractor

    init {
        val scope = getKoin().getOrCreateScope(
            Scope.MAIN_MODULE_SCOPE.name,
            named(Scope.MAIN_MODULE_SCOPE.name)
        )
        interactor = scope.get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    override fun onDestroy() {
        super.onDestroy()
        getKoin().deleteScope(Scope.MAIN_MODULE_SCOPE.name)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        interactor.initialize(this, this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }
}
