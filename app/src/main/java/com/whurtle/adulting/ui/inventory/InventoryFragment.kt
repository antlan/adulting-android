package com.whurtle.adulting.ui.inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.whurtle.adulting.R
import com.whurtle.adulting.databinding.InventoryFragmentBinding
import org.koin.android.ext.android.getKoin
import org.koin.core.qualifier.named


interface IInventoryView {
}

class InventoryFragment : IInventoryView, Fragment() {

    private lateinit var binding: InventoryFragmentBinding

    private lateinit var presenter: IInventoryPresenter
    private lateinit var router: IInventoryRouter
    private lateinit var interactor: IInventoryInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scope = getKoin().createScope(
            Scope.INVENTORY_MODULE_SCOPE.name,
            named(Scope.INVENTORY_MODULE_SCOPE.name)
        )
        presenter = scope.get()
        router = scope.get()
        interactor = scope.get()
    }

    override fun onDestroy() {
        super.onDestroy()
        getKoin().deleteScope(Scope.INVENTORY_MODULE_SCOPE.name)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = InventoryFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        router.setWeakReference(this)
        initializeView()
    }

    fun initializeView() {
        binding.inventoryCreateItemButton.setOnClickListener {
            interactor.onCreateItemButtonClicked()
        }
    }
}