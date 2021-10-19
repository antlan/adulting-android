package com.whurtle.adulting

import android.app.Application
import android.os.Bundle
import com.whurtle.adulting.store.appRoomDatabaseModule
import com.whurtle.adulting.store.grocery.groceryStoreModule
import com.whurtle.adulting.ui.dashboard.dashboardModule
import com.whurtle.adulting.ui.grocery.groceryModule
import com.whurtle.adulting.ui.inventory.inventoryModule
import com.whurtle.adulting.ui.inventory.inventoryStoreModule
import com.whurtle.adulting.ui.inventory.item.create.createItemModule
import com.whurtle.adulting.ui.inventory.item.view.viewItemModule
import com.whurtle.adulting.ui.main.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                // UI
                mainModule,
                dashboardModule,
                inventoryModule,
                createItemModule,
                viewItemModule,
                groceryModule,
                // Room Database
                appRoomDatabaseModule,
                // Stores
                inventoryStoreModule,
                groceryStoreModule
            )
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

    }


}


