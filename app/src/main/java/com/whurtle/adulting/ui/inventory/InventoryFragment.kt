package com.whurtle.adulting.ui.inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.whurtle.adulting.databinding.InventoryFragmentBinding
import com.whurtle.adulting.databinding.InventoryFragmentListItemBinding
import com.whurtle.adulting.store.inventory.Item
import com.whurtle.adulting.ui.common.utils.QuantityUtils
import org.koin.android.ext.android.getKoin
import org.koin.core.qualifier.named
import timber.log.Timber


interface IInventoryView {

    fun setInventoryListItems(items: List<Item>)
    fun clearListItems()
    fun showMessage(message: String)

}

class InventoryFragment : Fragment(), IInventoryView, OnItemClickListener,
    OnAddItemToGroceryListClickedListener {

    private lateinit var interactor: IInventoryInteractor

    private lateinit var binding: InventoryFragmentBinding
    private var adapter = InventoryItemListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scope = getKoin().getOrCreateScope(
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
        binding = InventoryFragmentBinding.inflate(layoutInflater, container, false)
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
        binding.inventoryCreateItemButton.setOnClickListener {
            interactor.onCreateItemButtonClicked()
        }
        binding.itemList.layoutManager = LinearLayoutManager(this.requireContext())
        binding.itemList.adapter = adapter
        adapter.setItemClickListener(this)
        adapter.setAddItemToGroceryListClickedListener(this)
    }

    override fun setInventoryListItems(items: List<Item>) {
        Timber.d("From db ${items.size}")
        with(adapter) {
            setItems(items)
        }
    }

    override fun clearListItems() {
    }

    override fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()

    }

    override fun onClick(item: Item) {
        interactor.onItemClicked(item)
    }

    override fun onAddItemToGroceryListClick(item: Item) {
        interactor.onAddItemToGroceryListClicked(item)
    }
}


class InventoryItemListAdapter : RecyclerView.Adapter<ItemViewHolder>() {

    private val list: ArrayList<String> = ArrayList()
    private val map: HashMap<String, Item> = HashMap()
    private var listener: OnItemClickListener? = null
    private var onAddToGroceryListener: OnAddItemToGroceryListClickedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = InventoryFragmentListItemBinding.inflate(LayoutInflater.from(parent.context))
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = map[list[position]]!!
        holder.bind(item)
        holder.binding.root.setOnClickListener {
            Timber.d("bind clicked")
            listener!!.onClick(item)
        }
        holder.binding.addToGroceryListButton.setOnClickListener {
            Timber.d("item clicked to add to grocery list")
            onAddToGroceryListener!!.onAddItemToGroceryListClick(item)
        }
    }

    override fun getItemCount(): Int {
        return map.size
    }

    fun setItems(items: List<Item>) {
        map.clear()
        list.clear()
        for (item in items) {
            map[item.id] = item
            list.add(item.id)
        }
        notifyDataSetChanged()
    }

    fun setItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun setAddItemToGroceryListClickedListener(listener: OnAddItemToGroceryListClickedListener) {
        this.onAddToGroceryListener = listener
    }
}

class ItemViewHolder(var binding: InventoryFragmentListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Item) {
        binding.name.text = item.name
        binding.stockCount.text = QuantityUtils.formatQuantity(item.quantity)
        Timber.d("binding item ${item.name}")
    }
}

interface OnItemClickListener {
    fun onClick(item: Item)
}

interface OnAddItemToGroceryListClickedListener {
    fun onAddItemToGroceryListClick(item: Item)
}
