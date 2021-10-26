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
) : Parcelable {

    companion object Utils {
        fun normalizeNumber(number: Float): String {
            return when {
                number == 0.0f -> {
                    "0"
                }
                number % 1.0 == 0.0 -> {
                    String.format("%d", number.toLong())
                }
                else -> {
                    String.format("%0.2f", number)
                }
            }
        }
    }
}