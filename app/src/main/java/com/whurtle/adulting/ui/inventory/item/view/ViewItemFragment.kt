package com.whurtle.adulting.ui.inventory.item.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.whurtle.adulting.databinding.ViewItemFragmentBinding
import android.widget.ArrayAdapter
import com.whurtle.adulting.store.inventory.Item
import org.koin.android.ext.android.getKoin
import org.koin.core.qualifier.named


interface IViewItemView {
    fun populateView(item: Item?)
}

class ViewItemFragment : IViewItemView, Fragment() {

    private lateinit var binding: ViewItemFragmentBinding

    private lateinit var interactor: IViewItemInteractor

    companion object {
        val KEY_ITEM = "item"

        fun newInstance(item: Item): ViewItemFragment {
            val fragment = ViewItemFragment()
            val bundle = Bundle()
            bundle.putParcelable(KEY_ITEM, item)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scope = getKoin().getOrCreateScope(
            Scope.VIEW_ITEM_MODULE_SCOPE.name,
            named(Scope.VIEW_ITEM_MODULE_SCOPE.name)
        )
        interactor = scope.get()

        val item: Item? = arguments?.getParcelable(KEY_ITEM)
        interactor.setItemEntry(item)
    }

    override fun onDestroy() {
        getKoin().deleteScope(Scope.VIEW_ITEM_MODULE_SCOPE.name)
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ViewItemFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        interactor.initialize(this, this)
        initializeView()
    }

    fun initializeView() {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(this.requireContext(), android.R.layout.simple_list_item_1)
        adapter.add("Apple")
        binding.nameInput.setAdapter(adapter)
        binding.submit.setOnClickListener {
            val name = binding.nameInput.text.toString()
            val extra = binding.extra.editText?.text.toString()
            val quantity = binding.quantity.editText?.text.toString()
            interactor.updateItem(name, extra, quantity)
        }

        binding.reset.setOnClickListener {
            interactor.resetForm()
        }
    }

    override fun populateView(item: Item?) {
        binding.nameInput.setText(item?.name)
        binding.extraInput.setText(item?.extra)

        var formattedQuantity = "0"
        if (item!!.quantity % 1.0 == 0.0) {
            formattedQuantity = String.format("%d", item.quantity.toLong())
        } else if (item.quantity > 0) {
            formattedQuantity = String.format("%0.2f", item.quantity)
        }

        binding.quantityInput.setText(formattedQuantity)
    }
}