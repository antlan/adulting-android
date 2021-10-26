package com.whurtle.adulting.ui.grocery

import android.content.ClipData
import androidx.fragment.app.Fragment
import androidx.room.ColumnInfo
import com.whurtle.adulting.store.grocery.GroceryEntry
import com.whurtle.adulting.store.grocery.GroceryItem
import com.whurtle.adulting.store.grocery.IGroceryStore
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.java.KoinJavaComponent.getKoin

interface IGroceryInteractor {
    fun initialize(fragment: Fragment, view: IGroceryView)
    fun onCreateItemButtonClicked()
    fun onItemClicked(item: GroceryItem)
    fun onRefreshItems()
    fun onGroceryItemUpdateClicked(groceryEntry: GroceryEntry)
    fun onGroceryItemDeleteClicked(groceryEntry: GroceryEntry)
}

class GroceryInteractor : IGroceryInteractor {

    private var router: IGroceryRouter
    private var presenter: IGroceryPresenter

    private var groceryStore: IGroceryStore

    private var inventoryListPage: Int = 0
    private var inventoryListPageSize: Int = 100

    private var disposables: CompositeDisposable = CompositeDisposable()

    init {
        var scope = getKoin().getScope(Scope.INVENTORY_MODULE_SCOPE.name)
        router = scope.get()
        presenter = scope.get()
        groceryStore = getKoin().get()
    }

    override fun initialize(fragment: Fragment, view: IGroceryView) {
        presenter.setView(view)
        router.setWeakReference(fragment)
    }

    override fun onCreateItemButtonClicked() {
        router.gotoCreateItemView()
    }

    override fun onItemClicked(item: GroceryItem) {
        presenter.showUpdateOptions(item)
    }

    override fun onRefreshItems() {
        var limit = inventoryListPageSize
        var offset = inventoryListPageSize * inventoryListPage
        disposables.add(groceryStore.getAllItems(
            limit, offset
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                presenter.setInventoryListItems(list)
            }
        )
    }

    override fun onGroceryItemUpdateClicked(
        groceryEntry: GroceryEntry
    ) {
        disposables.add(groceryStore.updateItem(groceryEntry)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                presenter.refreshItemDuetoUpdate(groceryEntry)
            }
        )
    }

    override fun onGroceryItemDeleteClicked(groceryEntry: GroceryEntry) {
        disposables.add(groceryStore.deleteItem(groceryEntry.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                presenter.refreshItemDuetoDelete(groceryEntry)
            }
        )
    }
}