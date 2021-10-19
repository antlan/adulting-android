package com.whurtle.adulting.store.grocery

import android.os.Parcelable
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.whurtle.adulting.store.inventory.Item
import kotlinx.parcelize.Parcelize


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
data class GroceryItem(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "target_quantity") val targetQuantity: Float,
    @ColumnInfo(name = "item_id") val itemId: String,
) : Parcelable

@Parcelize
data class GroceryItemFull(
    @Embedded val groceryItem: GroceryItem,

    @Relation(
        parentColumn = "item_id",
        entityColumn = "id"
    )
    val item: Item
) : Parcelable
