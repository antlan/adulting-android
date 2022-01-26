package com.whurtle.adulting.ui.inventory.item.view

import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.whurtle.adulting.store.inventory.IInventoryStore
import com.whurtle.adulting.store.inventory.Item
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDate
import org.koin.java.KoinJavaComponent.getKoin
import timber.log.Timber
import java.util.*

interface IViewItemInteractor {
    fun initialize(fragment: Fragment, view: IViewItemView)
    fun setItemEntry(item: Item?)
    fun deleteItem(item: Item?)
    fun updateItem(
        name: String,
        extra: String,
        quantity: String,
        displayUnit: String?,
        displayIcon: String?,
        criticalQuantity: String?,
        targetQuantity: String?,
        consumeBy: String?
    )

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

    override fun deleteItem(item: Item?) {
        disposables.add(
            inventoryStore.deleteItem(item!!.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    presenter.showMessage("Item Deleted")
                    router.goBack()
                }
                .doOnError {
                    presenter.showError(it.message!!)
                }
                .subscribe()
        )
    }

    override fun updateItem(
        name: String,
        extra: String,
        quantity: String,
        displayUnit: String?,
        displayIcon: String?,
        criticalQuantity: String?,
        targetQuantity: String?,
        consumeBy: String?
    ) {

        // Validation

        var valid = true

        var resolvedQuantity = 0f
        if (!TextUtils.isEmpty(quantity) && !TextUtils.isDigitsOnly(
                quantity.replaceFirst(
                    ".",
                    ""
                )
            )
        ) {
            presenter.showError("Quantity should be a number")
            valid = false
        } else if (!TextUtils.isEmpty(quantity)) {
            resolvedQuantity = quantity.toFloat()
        }

        var resolvedCriticalQuantity: Float? = null
        if (!TextUtils.isEmpty(criticalQuantity) && !TextUtils.isDigitsOnly(
                criticalQuantity!!.replaceFirst(
                    ".",
                    ""
                )
            )
        ) {
            presenter.showError("Critical Quantity should be a number")
            valid = false
        } else if (!TextUtils.isEmpty(criticalQuantity)) {
            resolvedCriticalQuantity = criticalQuantity!!.toFloat()
        }

        var resolvedTargetQuantity: Float? = null
        if (!TextUtils.isEmpty(targetQuantity) && !TextUtils.isDigitsOnly(
                targetQuantity!!.replaceFirst(
                    ".",
                    ""
                )
            )
        ) {
            presenter.showError("Target Quantity should be a number")
            valid = false
        } else if (!TextUtils.isEmpty(targetQuantity)) {
            resolvedCriticalQuantity = targetQuantity!!.toFloat()
        }

        var resolvedExtra: String? = null
        if (!TextUtils.isEmpty(extra)) {
            resolvedExtra = extra
        }

        var resolvedDisplayUnit: String? = null
        if (!TextUtils.isEmpty(displayUnit)) {
            resolvedDisplayUnit = displayUnit
        }

        var resolvedConsumeByEpoch: Long? = null

        if (!TextUtils.isEmpty(consumeBy)) {
            resolvedConsumeByEpoch =
                consumeBy!!.toLocalDate().atStartOfDayIn(TimeZone.currentSystemDefault())
                    .toEpochMilliseconds()
        }

        val resolvedLastUpdatedEpoch = Clock.System.now().toEpochMilliseconds()

        var displayIconId = null //displayIcon?.toLong()

        if (valid) {
            var item = Item(
                item!!.id,
                name,
                resolvedExtra,
                resolvedQuantity,
                resolvedDisplayUnit,
                displayIconId,
                resolvedCriticalQuantity,
                resolvedTargetQuantity,
                resolvedConsumeByEpoch,
                resolvedLastUpdatedEpoch
            )

            Timber.d("Item: $item")

            disposables.add(
                inventoryStore.updateItem(item)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete {
                        presenter.showMessage("Item Created")
                        router.goBack()
                    }
                    .doOnError {
                        presenter.showError(it.message!!)
                    }
                    .subscribe()
            )
            Timber.d("Item Created")
        }
    }

    override fun resetForm() {
        presenter.populateView(item)
    }

}