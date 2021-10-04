package com.whurtle.adulting.store.inventory

interface IInventoryStore {

    fun createItem(item: Item)
    fun getItem(id: String): Item?
    fun deleteItem(id: String): Item?
    fun updateItem(item: Item)

    fun getAllItems(): ArrayList<Item>
}

class InventoryStore : IInventoryStore {
    var items: HashMap<String, Item> = HashMap()

    override fun createItem(item: Item) {
        items[item.id] = item
    }

    override fun getItem(id: String): Item? {
        return items[id]
    }

    override fun deleteItem(id: String): Item? {
        return items.remove(id)
    }

    override fun updateItem(item: Item) {
        items[item.id] = item
    }

    override fun getAllItems(): ArrayList<Item> {
        return ArrayList(items.values)
    }
}