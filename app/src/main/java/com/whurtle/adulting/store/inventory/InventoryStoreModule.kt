package com.whurtle.adulting.ui.inventory

import com.whurtle.adulting.store.inventory.IInventoryStore
import com.whurtle.adulting.store.inventory.InventoryStore
import org.koin.dsl.module

val inventoryStoreModule = module {
    single<IInventoryStore> { InventoryStore() }
}