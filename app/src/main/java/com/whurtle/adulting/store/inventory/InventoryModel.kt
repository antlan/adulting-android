package com.whurtle.adulting.store.inventory

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item(
    val id: String,
    val name: String,
    val extra: String,
    val quantity: Float
) : Parcelable
