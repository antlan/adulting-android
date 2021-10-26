package com.whurtle.adulting.ui.inventory.item.view

import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.whurtle.adulting.store.inventory.IInventoryStore
import com.whurtle.adulting.store.inventory.Item
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.java.KoinJavaComponent.getKoin
import timber.log.Timber
import java.util.*

interface IViewItemInteractor {
    fun initialize(fragment: Fragment, view: IViewItemView)
    fun setItemEntry(item: Item?)
    fun updateItem(name: String, extra: String, quantity: String)
    fun shutdown()
    fun resetForm()
}

class ViewItemInteractor : IViewItemInteractor {

    private var item: Item? = null

    private var router: IViewItemRouter
    private var presenter: IViewItemPresenter
    private var inventoryStore: IInventoryStore
    private var disposables: CompositeDisposable = CompositeDisposable()

    init {
        var scope = getKoin().getScope(Scope.VIEW_ITEM_MODULE_SCOPE.name)
        router = scope.get()
        presenter = scope.get()
        inventoryStore = getKoin().get()
    }

    override fun shutdown() {
        disposables.dispose()
    }


    override fun initialize(fragment: Fragment, view: IViewItemView) {
        router.setWeakReference(fragment)
        presenter.setView(view)
        presenter.populateView(item)
    }

    override fun setItemEntry(item: Item?) {
        this.item = item
    }

    override fun updateItem(name: String, extra: String, quantity: String) {

        var valid = true

        if (TextUtils.isEmpty(quantity) || !TextUtils.isDigitsOnly(quantity)) {
            presenter.showError("Quantity should be a number")
            valid = false
        }

        if (valid) {
            var updateItem = Item(
                item!!.id,
                name,
                extra,
                quantity.toFloat()
            )

            Timber.d("Item: $updateItem")

            disposables.add(
                inventoryStore.updateItem(updateItem)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete {
                        presenter.showMessage("Item Updated")
                        router.goBack()
                    }
                    .doOnError {
                        presenter.showError(it.message!!)
                    }
                    .subscribe()
            )
            Timber.d("Item Updated")
        }
    }

    override fun resetForm() {
        presenter.populateView(item)
    }

}