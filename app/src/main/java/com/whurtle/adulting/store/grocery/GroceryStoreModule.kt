package com.whurtle.adulting.store.grocery

import org.koin.dsl.module

val groceryStoreModule = module {
    single<IGroceryStore> { GroceryStore() }
}