package com.whurtle.adulting.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.whurtle.adulting.databinding.DashboardFragmentBinding
import org.koin.android.ext.android.getKoin
import org.koin.core.qualifier.named

interface IDashboardView {
}

class DashboardFragment : IDashboardView, Fragment() {

    private lateinit var binding: DashboardFragmentBinding

    private lateinit var presenter: IDashboardPresenter
    private lateinit var router: IDashboardRouter
    private lateinit var interactor: IDashboardInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        binding = DashboardFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
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
        binding.inventoryButton.setOnClickListener {
            interactor.onInventoryButtonClicked()
        }
    }

}