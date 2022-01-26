package com.whurtle.adulting.ui.inventory.item.create

import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.whurtle.adulting.store.inventory.IInventoryStore
import com.whurtle.adulting.store.inventory.Item
import com.whurtle.adulting.ui.dashboard.IDashboardView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.getKoin
import org.w3c.dom.Text
import timber.log.Timber
import java.util.*

interface ICreateItemInteractor {

    fun initialize(fragment: Fragment, view: ICreateItemView)

    fun createItem(
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
}

class CreateItemInteractor : ICreateItemInteractor {

    private var router: ICreateItemRouter
    private var presenter: ICreateItemPresenter
    private var inventoryStore: IInventoryStore
    private var disposables: CompositeDisposable = CompositeDisposable()

    init {
        var scope = getKoin().getScope(Scope.CREATE_ITEM_MODULE_SCOPE.name)
        router = scope.get()
        presenter = scope.get()
        inventoryStore = getKoin().get()
    }

    override fun shutdown() {
        disposables.dispose()
    }

    override fun initialize(fragment: Fragment, view: ICreateItemView) {
        router.setWeakReference(fragment)
        presenter.setView(view)
    }

    override fun createItem(
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
                UUID.randomUUID().toString(),
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
                inventoryStore.createItem(item)
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

}