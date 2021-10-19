package com.whurtle.adulting.store.inventory

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "item")
data class Item(

    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "extra") val extra: String?,
    @ColumnInfo(name = "quantity") val quantity: Float
) : Parcelable