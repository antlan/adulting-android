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
}

class ViewItemFragment : IViewItemView, Fragment() {


    private var item: Item? = null

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
        val scope = getKoin().createScope(
            Scope.VIEW_ITEM_MODULE_SCOPE.name,
            named(Scope.VIEW_ITEM_MODULE_SCOPE.name)
        )
        interactor = scope.get()

        item = arguments?.getParcelable(KEY_ITEM)

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
        binding.nameInput.setText(item?.name)
        binding.nameInput.setAdapter(adapter)
        binding.submit.setOnClickListener {
            val name = binding.nameInput.text.toString()
            val extra = binding.extra.editText?.text.toString()
            val quantity = binding.quantity.editText?.text.toString()
            interactor.createItem(name, extra, quantity)
        }
    }
}