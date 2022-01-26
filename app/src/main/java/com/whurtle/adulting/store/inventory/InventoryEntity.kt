package com.whurtle.adulting.store.inventory

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.DecimalFormat


@Parcelize
@Entity(tableName = "item")
data class Item(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "extra") val extra: String?,
    @ColumnInfo(name = "quantity") val quantity: Float,
    @ColumnInfo(name = "display_unit") val displayUnit: String?,
    @ColumnInfo(name = "display_icon") val displayIcon: Long?,
    @ColumnInfo(name = "critical_quantity") val criticalQuantity: Float?,
    @ColumnInfo(name = "target_quantity") val targetQuantity: Float?,
    @ColumnInfo(name = "consume_by") val consumeBy: Long?,
    @ColumnInfo(name = "last_modified") val lastUpdated: Long?
) : Parcelable