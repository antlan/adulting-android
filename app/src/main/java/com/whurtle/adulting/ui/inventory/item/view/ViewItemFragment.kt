package com.whurtle.adulting.ui.inventory.item.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.whurtle.adulting.databinding.ViewItemFragmentBinding
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import com.whurtle.adulting.store.inventory.Item
import com.whurtle.adulting.ui.common.utils.QuantityUtils
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.android.ext.android.getKoin
import org.koin.core.qualifier.named


interface IViewItemView {
    fun showMessage(message: String)
    fun populateView(item: Item?)
}

class ViewItemFragment : IViewItemView, Fragment() {

    private lateinit var binding: ViewItemFragmentBinding

    private lateinit var interactor: IViewItemInteractor

    private lateinit var item: Item

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

        item = arguments?.getParcelable(KEY_ITEM)!!
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

    override fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    fun initializeView() {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(this.requireContext(), android.R.layout.simple_list_item_1)
        adapter.add("Apple")
        binding.nameInput.setAdapter(adapter)

        binding.delete.setOnClickListener {
            interactor.deleteItem(item)
        }

        binding.submit.setOnClickListener {
            val name = binding.nameInput.text.toString()
            val extra = binding.extra.editText?.text.toString()
            val quantity = binding.quantity.editText?.text.toString()
            val displayUnit = binding.displayUnit.editText?.text.toString()
            val displayIcon = binding.displayIcon.editText?.text.toString()
            val criticalQuantity = binding.criticalQuantity.editText?.text.toString()
            val targetQuantity = binding.targetQuantity.editText?.text.toString()
            val consumeBy = binding.consumeByDate.editText?.text.toString()
            interactor.updateItem(
                name,
                extra,
                quantity,
                displayUnit,
                displayIcon,
                criticalQuantity,
                targetQuantity,
                consumeBy
            )
        }

        binding.reset.setOnClickListener {
            interactor.resetForm()
        }
    }

    override fun populateView(item: Item?) {
        binding.nameInput.setText(item?.name)
        binding.extraInput.setText(item?.extra)

        binding.quantityInput.setText(QuantityUtils.formatQuantity(item?.quantity))

        binding.displayUnitInput.setText(item?.displayUnit)

        if (item?.displayIcon != null) {
            binding.displayIconInput.setText(item.displayIcon.toString())
        }

        binding.criticalQuantityInput.setText(QuantityUtils.formatQuantity(item?.criticalQuantity))
        binding.targetQuantityInput.setText(QuantityUtils.formatQuantity(item?.targetQuantity))


        if (item?.consumeBy != null) {
            val consumeByDate = Instant.fromEpochMilliseconds(item.consumeBy)
                .toLocalDateTime(TimeZone.currentSystemDefault())
            val dayOfMonth = consumeByDate.dayOfMonth
            val month = consumeByDate.monthNumber
            val year = consumeByDate.year
            binding.consumeByDateInput.setText(
                String.format(
                    "%04d-%02d-%02d",
                    year,
                    month,
                    dayOfMonth
                )
            )
        }
    }
}