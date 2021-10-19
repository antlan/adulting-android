package com.whurtle.adulting.ui.inventory

import android.database.sqlite.SQLiteConstraintException
import androidx.fragment.app.Fragment
import com.whurtle.adulting.store.grocery.GroceryItem
import com.whurtle.adulting.store.grocery.IGroceryStore
import com.whurtle.adulting.store.inventory.IInventoryStore
import com.whurtle.adulting.store.inventory.Item
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.getKoin
import timber.log.Timber
import java.util.*

interface IInventoryInteractor {
    fun initialize(fragment: Fragment, view: IInventoryView)
    fun onCreateItemButtonClicked()
    fun onItemClicked(item: Item)
    fun onAddItemToGroceryListClicked(item: Item)
    fun onRefreshItems()
}

class InventoryInteractor : IInventoryInteractor {

    private var router: IInventoryRouter
    private var presenter: IInventoryPresenter

    private var inventoryStore: IInventoryStore
    private var groceryStore: IGroceryStore

    private var inventoryListPage: Int = 0
    private var inventoryListPageSize: Int = 100

    private var disposables: CompositeDisposable = CompositeDisposable()

    init {
        var scope = getKoin().getScope(Scope.INVENTORY_MODULE_SCOPE.name)
        router = scope.get()
        presenter = scope.get()
        inventoryStore = getKoin().get()
        groceryStore = getKoin().get()
    }

    override fun initialize(fragment: Fragment, view: IInventoryView) {
        presenter.setView(view)
        router.setWeakReference(fragment)
    }

    override fun onCreateItemButtonClicked() {
        router.gotoCreateItemView()
    }

    override fun onItemClicked(item: Item) {
        router.gotoItemView(item)
    }

    override fun onAddItemToGroceryListClicked(item: Item) {
        Timber.d("onAddItemToGroceryListClicked {%s}", item.name)
        val groceryItem = GroceryItem(
            id = String.format("grocery0-%s", item.id),
            targetQuantity = 1.0f,
            itemId = item.id
        )

        disposables.add(groceryStore.createItem(groceryItem)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    presenter.showMessage("Item Added to List")
                },
                {
                    if (it::class == SQLiteConstraintException::class) {
                        presenter.showMessage("Item already added to the list")
                    } else {
                        presenter.showMessage("Encountered an issue while adding this item to the list")
                    }
                }
            )
        )
    }

    override fun onRefreshItems() {
        var limit = inventoryListPageSize
        var offset = inventoryListPageSize * inventoryListPage
        disposables.add(inventoryStore.getAllItems(
            limit, offset
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                presenter.setInventoryListItems(list)
            }
        )
    }
}