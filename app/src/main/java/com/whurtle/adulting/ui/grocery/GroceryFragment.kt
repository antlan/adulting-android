package com.whurtle.adulting.ui.grocery

import android.content.res.ColorStateList
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import com.whurtle.adulting.R
import com.whurtle.adulting.databinding.GroceryFragmentBinding
import com.whurtle.adulting.databinding.GroceryFragmentListItemBinding
import com.whurtle.adulting.databinding.GroceryItemUpdateOptionsDialogFragmentBinding
import com.whurtle.adulting.store.grocery.GroceryEntry
import com.whurtle.adulting.store.grocery.GroceryItem
import com.whurtle.adulting.store.inventory.Item
import org.koin.android.ext.android.getKoin
import org.koin.core.qualifier.named
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


interface IGroceryView {

    fun setInventoryListItems(items: List<GroceryItem>)
    fun clearListItems()
    fun showUpdateOptions(groceryEntry: GroceryItem);
    fun updateGroceryItem(groceryEntry: GroceryEntry)
    fun deleteGroceryItem(groceryEntry: GroceryEntry)
    fun showProcessingComplete()
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
//        val itemSwipeHelper = ItemSwipeHelper()
//        ItemTouchHelper(itemSwipeHelper).attachToRecyclerView(binding.itemList)

        binding.done.setOnClickListener {
            interactor.onDoneClicked()
        }


    }

    override fun setInventoryListItems(items: List<GroceryItem>) {
        Timber.d("From db ${items.size}")
        adapter.setItems(items)
        updateStatusCounters()
    }

    override fun clearListItems() {
        updateStatusCounters()
    }

    override fun showUpdateOptions(groceryEntry: GroceryItem) {
        var dialog = GroceryItemUpdateOptionsDialogFragment(groceryEntry, interactor)
        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFullScreen)
        dialog.show(childFragmentManager, GroceryItemUpdateOptionsDialogFragment.TAG)
    }

    override fun updateGroceryItem(groceryEntry: GroceryEntry) {
        adapter.updateItemWithId(groceryEntry)
        updateStatusCounters()
    }

    override fun deleteGroceryItem(groceryEntry: GroceryEntry) {
        adapter.deleteItemWithId(groceryEntry)
        updateStatusCounters()
    }

    override fun showProcessingComplete() {
        updateStatusCounters()
        interactor.resetList()
    }

    override fun onClick(item: GroceryItem) {
        interactor.onItemClicked(item)
    }

    fun updateStatusCounters() {
        val total = adapter.itemCount
        val done = adapter.getItemWithStatusCount(GroceryEntry.GROCERY_ENTRY_STATUS_DONE)
        val drop = adapter.getItemWithStatusCount(GroceryEntry.GROCERY_ENTRY_STATUS_DROP)

        binding.itemsTotal.text = String.format(
            "%s/%s", (done + drop).toString(), total.toString()
        )
        binding.itemsDone.text =
            adapter.getItemWithStatusCount(GroceryEntry.GROCERY_ENTRY_STATUS_DONE).toString()
        binding.itemsCancelled.text =
            adapter.getItemWithStatusCount(GroceryEntry.GROCERY_ENTRY_STATUS_DROP).toString()
    }

}


class InventoryItemListAdapter : RecyclerView.Adapter<ItemViewHolder>() {

    private val list: ArrayList<String> = ArrayList()
    private val map: HashMap<String, GroceryItem> = HashMap()
    private var listener: OnItemClickListener? = null
    private var comparator = ItemComparator(map)

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
        sort()
        notifyDataSetChanged()
    }

    fun setItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun updateItemWithId(groceryEntry: GroceryEntry) {
        val groceryItem = map[groceryEntry.id]
        val groceryItemUpdate = groceryItem!!.copy(groceryEntry = groceryEntry)
        map[groceryEntry.id] = groceryItemUpdate
        var oldIndex = list.indexOf(groceryEntry.id)
        sort()
        val newIndex = list.indexOf(groceryEntry.id)
        if (oldIndex != newIndex) {
            notifyItemMoved(oldIndex, newIndex)
        }
        notifyItemChanged(newIndex)
    }

    fun deleteItemWithId(groceryEntry: GroceryEntry) {
        map.remove(groceryEntry.id)
        val index = list.indexOf(groceryEntry.id)
        list.remove(groceryEntry.id)
        notifyItemRemoved(index)
    }

    fun sort() {
//        var unsortedList = ArrayList<String>(list)
        Collections.sort(list, comparator)
//        var sortedList = ArrayList<String>(list)
//
//        for (item in list) {
//            Timber.d("%d, %d", oldIndex, newIndex)
//            if (oldIndex != newIndex) {
//                notifyItemMoved(oldIndex, newIndex)
//                unsortedList.remove(item)
//            }
//        }
    }

    fun itemMoved(from: Int, to: Int) {
        Collections.swap(list, from, to)
        notifyItemMoved(from, to)
    }

    fun getItemWithStatusCount(status: String): Int {
        var statusCount = 0
        for (itemId in list) {
            var itemStatus = map[itemId]!!.groceryEntry.status
            if (status == itemStatus) {
                statusCount += 1
            }
        }
        return statusCount
    }

    class ItemComparator(var map: HashMap<String, GroceryItem>) : Comparator<String> {

        var itemOrder: ArrayList<String> = ArrayList()

        init {
            itemOrder.add(GroceryEntry.GROCERY_ENTRY_STATUS_PENDING)
            itemOrder.add(GroceryEntry.GROCERY_ENTRY_STATUS_DONE)
            itemOrder.add(GroceryEntry.GROCERY_ENTRY_STATUS_DROP)
        }

        override fun compare(item1: String?, item2: String?): Int {
            var status1 = itemOrder.indexOf(map[item1]!!.groceryEntry.status)
            var status2 = itemOrder.indexOf(map[item2]!!.groceryEntry.status)
            var name1 = map[item1]!!.item.name
            var name2 = map[item2]!!.item.name

            var result = status1.compareTo(status2)
            if (result == 0) {
                result = name1.compareTo(name2)
            }
            return result
        }
    }
}

class ItemViewHolder(var binding: GroceryFragmentListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: GroceryItem) {
        binding.name.text = item.item.name
        val quantity = item.groceryEntry.targetQuantity
        binding.stockCount.text = Item.normalizeNumber(quantity)

        when {
            item.groceryEntry.status == GroceryEntry.GROCERY_ENTRY_STATUS_DONE -> {
                binding.status.setImageResource(R.drawable.ic_list_done)
                binding.status.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.colorPositiveGreen
                    )
                )
            }
            item.groceryEntry.status == GroceryEntry.GROCERY_ENTRY_STATUS_DROP -> {
                binding.status.setImageResource(R.drawable.ic_list_drop)
                binding.status.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.colorNegativeRed
                    )
                )
            }
            else -> {
                binding.status.setImageResource(0)
            }

        }
        Timber.d("binding item ${item.item.name}")

    }
}

interface OnItemClickListener {
    fun onClick(item: GroceryItem)
}
//
//class ItemSwipeHelper :
//    ItemTouchHelper.SimpleCallback(
//        ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
//        0
//    ) {
//    override fun onMove(
//        recyclerView: RecyclerView,
//        viewHolder: RecyclerView.ViewHolder,
//        target: RecyclerView.ViewHolder
//    ): Boolean {
//        val adapter = recyclerView.adapter as InventoryItemListAdapter
//        val from = viewHolder.adapterPosition
//        val to = target.adapterPosition
//        Timber.d("moving %d, %d", from, to)
//        adapter.itemMoved(from, to)
//        return true
//    }
//
//    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//        //nothing
//        Timber.d("swiped")
//    }
//}

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