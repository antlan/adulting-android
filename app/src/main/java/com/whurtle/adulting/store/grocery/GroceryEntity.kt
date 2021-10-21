package com.whurtle.adulting.store.grocery

import android.os.Parcelable
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.whurtle.adulting.store.inventory.Item
import kotlinx.parcelize.Parcelize


const val GROCERY_ENTRY_STATUS_PENDING = "pending"
const val GROCERY_ENTRY_STATUS_DONE = "done"
const val GROCERY_ENTRY_STATUS_DROP = "drop"

@Parcelize
@Entity(
    tableName = "grocery_item",
    foreignKeys = [
        ForeignKey(
            entity = Item::class,
            parentColumns = ["id"],
            childColumns = ["item_id"],
            onDelete = CASCADE
        )
    ],
    indices = [Index("item_id")]
)
data class GroceryEntry(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "target_quantity") val targetQuantity: Float,
    @ColumnInfo(name = "item_id") val itemId: String,
    @ColumnInfo(name = "status", defaultValue = GROCERY_ENTRY_STATUS_PENDING) val status: String,
) : Parcelable

@Parcelize
data class GroceryItem(
    @Embedded val groceryEntry: GroceryEntry,

    @Relation(
        parentColumn = "item_id",
        entityColumn = "id"
    )
    val item: Item
) : Parcelable
