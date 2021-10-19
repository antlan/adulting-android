package com.whurtle.adulting.ui.grocery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.whurtle.adulting.databinding.GroceryFragmentBinding
import com.whurtle.adulting.databinding.GroceryFragmentListItemBinding
import com.whurtle.adulting.databinding.InventoryFragmentBinding
import com.whurtle.adulting.databinding.InventoryFragmentListItemBinding
import com.whurtle.adulting.store.grocery.GroceryItem
import com.whurtle.adulting.store.grocery.GroceryItemFull
import com.whurtle.adulting.store.inventory.Item
import org.koin.android.ext.android.getKoin
import org.koin.core.qualifier.named
import timber.log.Timber


interface IGroceryView {

    fun setInventoryListItems(items: List<GroceryItemFull>)
    fun clearListItems()

}

class GroceryFragment : Fragment(), IGroceryView, OnItemClickListener {

    private lateinit var interactor: IGroceryInteractor

    private lateinit var binding: GroceryFragmentBinding
    private var adapter = InventoryItemListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scope = getKoin().createScope(
            Scope.INVENTORY_MODULE_SCOPE.name,
            named(Scope.INVENTORY_MODULE_SCOPE.name)
        )
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
        binding = GroceryFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeView()
        interactor.initialize(this, this)
    }

    override fun onResume() {
        super.onResume()
        interactor.onRefreshItems()
    }

    fun initializeView() {
        binding.itemList.layoutManager = LinearLayoutManager(this.requireContext())
        binding.itemList.adapter = adapter
        adapter.setItemClickListener(this)
    }

    override fun setInventoryListItems(items: List<GroceryItemFull>) {
        Timber.d("From db ${items.size}")
        with(adapter) {
            setItems(items)
        }
    }

    override fun clearListItems() {
    }

    override fun onClick(item: GroceryItemFull) {
        interactor.onItemClicked(item)
    }
}


class InventoryItemListAdapter : RecyclerView.Adapter<ItemViewHolder>() {

    private val list: ArrayList<GroceryItemFull> = ArrayList()
    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = GroceryFragmentListItemBinding.inflate(LayoutInflater.from(parent.context))
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
        holder.binding.name.setOnClickListener {
            Timber.d("bind clicked")
            listener!!.onClick(item)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setItems(items: List<GroceryItemFull>) {
        list.clear()
        list.addAll(items)
        notifyDataSetChanged()
    }

    fun setItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}

class ItemViewHolder(var binding: GroceryFragmentListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: GroceryItemFull) {
        binding.name.text = item.item.name
//        if (item.quantity == 0.0f) {
//            binding.stockCount.text = "x0"
//        } else if (item.quantity % 1.0 == 0.0) {
//            binding.stockCount.text = String.format("x%d", item.quantity.toLong())
//        } else {
//            binding.stockCount.text = String.format("x%0.2f", item.quantity)
//        }

        Timber.d("binding item ${item.item.name}")
    }
}

interface OnItemClickListener {
    fun onClick(item: GroceryItemFull)
}

