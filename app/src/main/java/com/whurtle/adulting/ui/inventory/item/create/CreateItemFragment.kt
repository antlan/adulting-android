package com.whurtle.adulting.ui.inventory.item.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import com.whurtle.adulting.R
import com.whurtle.adulting.databinding.CreateItemFragmentBinding
import android.widget.ArrayAdapter
import org.koin.android.ext.android.getKoin
import org.koin.core.qualifier.named


interface ICreateItemView {
}

class CreateItemFragment : ICreateItemView, Fragment() {

    private lateinit var binding: CreateItemFragmentBinding

    private lateinit var presenter: ICreateItemPresenter
    private lateinit var router: ICreateItemRouter
    private lateinit var interactor: ICreateItemInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scope = getKoin().createScope(
            Scope.CREATE_ITEM_MODULE_SCOPE.name,
            named(Scope.CREATE_ITEM_MODULE_SCOPE.name)
        )
        presenter = scope.get()
        router = scope.get()
        interactor = scope.get()
    }

    override fun onDestroy() {
        super.onDestroy()
        getKoin().deleteScope(Scope.CREATE_ITEM_MODULE_SCOPE.name)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CreateItemFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    fun initializeView() {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(this.requireContext(), android.R.layout.simple_list_item_1)
        adapter.add("Apple")
        adapter.add("Mango")
        binding.nameInput.setAdapter(adapter)
        binding.submit.setOnClickListener {
            val name = binding.nameInput.text.toString()
            val extra = binding.extra.editText?.text.toString()
            val quantity = binding.quantity.editText?.text.toString()
            interactor.createItem(name, extra, quantity)
        }
    }
}