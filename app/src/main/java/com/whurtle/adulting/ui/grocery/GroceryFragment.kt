package com.whurtle.adulting.ui.grocery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.whurtle.adulting.databinding.GroceryFragmentBinding
import com.whurtle.adulting.databinding.GroceryFragmentListItemBinding
import com.whurtle.adulting.databinding.GroceryItemUpdateOptionsDialogFragmentBinding
import com.whurtle.adulting.store.grocery.GroceryEntry
import com.whurtle.adulting.store.grocery.GroceryItem
import com.whurtle.adulting.store.inventory.Item
import org.koin.android.ext.android.getKoin
import org.koin.core.qualifier.named
import timber.log.Timber


interface IGroceryView {

    fun setInventoryListItems(items: List<GroceryItem>)
    fun clearListItems()
    fun showUpdateOptions(groceryEntry: GroceryItem);
    fun updateGroceryItem(groceryEntry: GroceryEntry)
    fun deleteGroceryItem(groceryEntry: GroceryEntry)
}

class GroceryFragment : Fragment(), IGroceryView, OnItemClickListener {

    private lateinit var interactor: IGroceryInteractor

    private lateinit var binding: GroceryFragmentBinding
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
        val itemSwipeHelper = ItemSwipeHelper()
        ItemTouchHelper(itemSwipeHelper).attachToRecyclerView(binding.itemList)

    }

    override fun setInventoryListItems(items: List<GroceryItem>) {
        Timber.d("From db ${items.size}")
        with(adapter) {
            setItems(items)
        }
    }

    override fun clearListItems() {
    }

    override fun showUpdateOptions(groceryEntry: GroceryItem) {
        GroceryItemUpdateOptionsDialogFragment(groceryEntry, interactor).show(
            childFragmentManager, GroceryItemUpdateOptionsDialogFragment.TAG
        )
    }

    override fun updateGroceryItem(groceryEntry: GroceryEntry) {
        adapter.updateItemWithId(groceryEntry)
    }

    override fun deleteGroceryItem(groceryEntry: GroceryEntry) {
        adapter.deleteItemWithId(groceryEntry)
    }

    override fun onClick(item: GroceryItem) {
        interactor.onItemClicked(item)
    }
}


class InventoryItemListAdapter : RecyclerView.Adapter<ItemViewHolder>() {


    private val list: ArrayList<String> = ArrayList()
    private val map: HashMap<String, GroceryItem> = HashMap()
    private var listener: OnItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = GroceryFragmentListItemBinding.inflate(LayoutInflater.from(parent.context))
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder,
        position: Int
    ) {
        val item = map[list[position]]!!
        holder.bind(item)
        holder.binding.name.setOnClickListener {
            Timber.d("bind clicked %s", item.item.name)
            listener!!.onClick(item)
        }

    }

    override fun getItemCount(): Int {
        return map.size
    }

    fun setItems(items: List<GroceryItem>) {
        map.clear()
        list.clear()
        for (item in items) {
            map[item.groceryEntry.id] = item
            list.add(item.groceryEntry.id)
        }
        notifyDataSetChanged()
    }

    fun setItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun updateItemWithId(groceryEntry: GroceryEntry) {
        val groceryItem = map[groceryEntry.id]
        val groceryItemUpdate = groceryItem!!.copy(groceryEntry = groceryEntry)
        map[groceryEntry.id] = groceryItemUpdate
        notifyItemChanged(list.indexOf(groceryEntry.id))
    }

    fun deleteItemWithId(groceryEntry: GroceryEntry) {
        map.remove(groceryEntry.id)
        notifyItemRemoved(list.indexOf(groceryEntry.id))
    }
}

class ItemViewHolder(var binding: GroceryFragmentListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: GroceryItem) {
        binding.name.text = item.item.name
        binding.status.text = item.groceryEntry.status
        val quantity = item.groceryEntry.targetQuantity
        binding.stockCount.text = Item.normalizeNumber(quantity)

        Timber.d("binding item ${item.item.name}")
    }
}

interface OnItemClickListener {
    fun onClick(item: GroceryItem)
}

class ItemSwipeHelper :
    ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
        0
    ) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        Timber.d("moving")
        val adapter = recyclerView.adapter as InventoryItemListAdapter
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition
        adapter.notifyItemMoved(from, to)

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //nothing
        Timber.d("swiped")
    }
}

class GroceryItemUpdateOptionsDialogFragment(
    var item: GroceryItem,
    var interactor: IGroceryInteractor
) : DialogFragment() {

    private lateinit var binding: GroceryItemUpdateOptionsDialogFragmentBinding

    companion object {
        const val TAG = "UpdateOptionsDialogFragment"
    }

    val statusLabels = HashMap<String, String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            GroceryItemUpdateOptionsDialogFragmentBinding.inflate(layoutInflater, container, false)
        initializeView()
        return binding.root
    }

    private fun initializeView() {

        statusLabels["Acquired"] = GroceryEntry.GROCERY_ENTRY_STATUS_DONE
        statusLabels["Unavailable"] = GroceryEntry.GROCERY_ENTRY_STATUS_DROP
        statusLabels["Pending"] = GroceryEntry.GROCERY_ENTRY_STATUS_PENDING

        val adapter: ArrayAdapter<String> =
            ArrayAdapter(this.requireContext(), android.R.layout.simple_list_item_1)
        adapter.addAll(statusLabels.keys)

        binding.name.text = item.item.name
        binding.targetQuantity.setText(
            Item.normalizeNumber(item.groceryEntry.targetQuantity)
        )
        binding.increaseQuantity.setOnClickListener {
            var value = binding.targetQuantity.text.toString().toFloat()
            value += 1
            binding.targetQuantity.setText(Item.normalizeNumber(value))
        }

        binding.decreaseQuantity.setOnClickListener {
            var value = binding.targetQuantity.text.toString().toFloat()
            value -= 1
            if (value < 0) {
                value = 0f
            }
            binding.targetQuantity.setText(Item.normalizeNumber(value))
        }

        var currentStatus = item.groceryEntry.status
        for (key in statusLabels.keys) {
            if (statusLabels[key] == currentStatus) {
                currentStatus = key
            }
        }

        binding.statusChoices.setText(currentStatus)
        binding.statusChoices.setAdapter(adapter)

        binding.submit.setOnClickListener {
            var status = binding.statusChoices.text.toString()
            status = statusLabels[status].toString()

            var groceryEntry = GroceryEntry(
                id = item.groceryEntry.id,
                itemId = item.item.id,
                targetQuantity = binding.targetQuantity.text.toString().toFloat(),
                status = status
            )

            interactor.onGroceryItemUpdateClicked(groceryEntry)
            this.dismiss()
        }

        binding.delete.setOnClickListener {
            interactor.onGroceryItemDeleteClicked(item.groceryEntry)
            this.dismiss()
        }
    }

}