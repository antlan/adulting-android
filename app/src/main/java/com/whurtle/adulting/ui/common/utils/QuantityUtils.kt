package com.whurtle.adulting.ui.common.utils

class QuantityUtils {

    companion object {
        fun formatQuantity(quantity: Float?): String {
            var formattedQuantity = "0"
            if (quantity != null) {
                if (quantity % 1.0 == 0.0) {
                    formattedQuantity = String.format("%d", quantity.toLong())
                } else if (quantity > 0) {
                    formattedQuantity = String.format("%s", quantity)
                }
            }
            return formattedQuantity
        }
    }
}