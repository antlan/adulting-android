package com.whurtle.adulting.ui.inventory.item.create

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import com.whurtle.adulting.R
import com.whurtle.adulting.databinding.CreateItemFragmentBinding
import android.widget.ArrayAdapter
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import com.whurtle.adulting.ui.common.utils.DateUtils
import kotlinx.datetime.*
import org.koin.android.ext.android.getKoin
import org.koin.core.qualifier.named


interface ICreateItemView {
    fun showMessage(message: String)
}

class CreateItemFragment : ICreateItemView, Fragment() {

    private lateinit var binding: CreateItemFragmentBinding

    private lateinit var presenter: ICreateItemPresenter
    private lateinit var router: ICreateItemRouter
    private lateinit var interactor: ICreateItemInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scope = getKoin().getOrCreateScope(
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
        interactor.initialize(this, this)
        initializeView()
    }

    override fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    fun initializeView() {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(this.requireContext(), android.R.layout.simple_list_item_1)
        binding.nameInput.setAdapter(adapter)
        binding.submit.setOnClickListener {
            val name = binding.nameInput.text.toString()
            val extra = binding.extra.editText?.text.toString()
            val quantity = binding.quantity.editText?.text.toString()
            val displayUnit = binding.displayUnit.editText?.text.toString()
            val displayIcon = binding.displayIcon.editText?.text.toString()
            val criticalQuantity = binding.criticalQuantity.editText?.text.toString()
            val targetQuantity = binding.targetQuantity.editText?.text.toString()
            val consumeBy = binding.consumeByDate.editText?.text.toString()
            interactor.createItem(
                name, extra, quantity,
                displayUnit,
                displayIcon,
                criticalQuantity,
                targetQuantity,
                consumeBy
            )
        }

        binding.consumeByDatePicker.setOnClickListener {
            showDatePicker(binding.consumeByDateInput as EditText)
        }

    }

    fun showDatePicker(editText: EditText) {
        var listener = DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
            val stringDate = String.format("%04d-%02d-%02d", year, month, dayOfMonth)
            editText.setText(stringDate)
        }

        val now = Clock.System.now()
        var localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
        val dayOfMonth = localDateTime.dayOfMonth
        val year = localDateTime.year
        val month = localDateTime.monthNumber

        DatePickerDialog(
            requireContext(),
            listener,
            year,
            month,
            dayOfMonth
        ).show()
    }

}