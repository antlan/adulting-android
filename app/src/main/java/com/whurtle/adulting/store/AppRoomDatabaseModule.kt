package com.whurtle.adulting.store

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appRoomDatabaseModule = module {
    single { AppRoomDatabase.getInstance(androidContext()) }
}