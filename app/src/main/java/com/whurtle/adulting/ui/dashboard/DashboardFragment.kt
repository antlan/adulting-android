package com.whurtle.adulting.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.whurtle.adulting.R
import kotlinx.android.synthetic.main.fragment_dashboard.fragment_dashboard_inventory_button as inventoryButton
import org.koin.android.ext.android.getKoin
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.getKoin

interface IDashboardView {
}

class DashboardFragment : IDashboardView, Fragment() {

    val presenter: IDashboardPresenter
    val router: IDashboardRouter
    val interactor: IDashboardInteractor

    init {
        val scope = getKoin().createScope(
            Scope.DASHBOARD_MODULE_SCOPE.name,
            named(Scope.DASHBOARD_MODULE_SCOPE.name)
        )
        presenter = scope.get()
        router = scope.get()
        interactor = scope.get()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        router.setWeakReference(this)
        initializeView()
    }

    override fun onDestroy() {
        super.onDestroy()
        getKoin().deleteScope(Scope.DASHBOARD_MODULE_SCOPE.name)
    }

    fun initializeView() {
        inventoryButton.setOnClickListener {
            interactor.onInventoryButtonClicked()
        }
    }

}