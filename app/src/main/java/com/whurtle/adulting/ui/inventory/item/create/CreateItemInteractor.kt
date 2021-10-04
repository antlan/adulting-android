package com.whurtle.adulting.ui.inventory.item.create

import android.text.TextUtils
import com.whurtle.adulting.store.inventory.Item
import org.koin.java.KoinJavaComponent
import timber.log.Timber
import java.util.*

interface ICreateItemInteractor {
    fun createItem(name: String, extra: String, quantity: String)
}

class CreateItemInteractor : ICreateItemInteractor {

    private var router: ICreateItemRouter
    private var presenter: ICreateItemPresenter

    init {
        var scope = KoinJavaComponent.getKoin().getScope(Scope.CREATE_ITEM_MODULE_SCOPE.name)
        router = scope.get()
        presenter = scope.get()
    }


    override fun createItem(name: String, extra: String, quantity: String) {

        var valid = true

        if (!TextUtils.isDigitsOnly(quantity)) {
            presenter.showError("Quantity should be a number")
            valid = false
        }

        if (valid) {
            var item = Item(
                UUID.randomUUID().toString(),
                name,
                extra,
                quantity.toFloat()
            )

            Timber.d("Item: $item")
        }
    }

}