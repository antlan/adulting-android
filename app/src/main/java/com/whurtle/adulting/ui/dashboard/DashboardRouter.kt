package com.whurtle.adulting.ui.dashboard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.whurtle.adulting.R
import com.whurtle.adulting.ui.grocery.GroceryFragment
import com.whurtle.adulting.ui.inventory.InventoryFragment
import java.lang.ref.WeakReference

interface IDashboardRouter {
    fun setWeakReference(reference: Fragment)
    fun gotoInventoryView()
    fun gotoGroceryView()
}


class DashboardRouter : IDashboardRouter {

    var inventoryFragment: InventoryFragment? = null
    var groceryFragment: GroceryFragment? = null

    var viewWeakReference: WeakReference<Fragment>? = null

    override fun setWeakReference(reference: Fragment) {
        viewWeakReference = WeakReference(reference)
    }

    override fun gotoInventoryView() {
        kotlin.runCatching {
            if (inventoryFragment == null) {
                inventoryFragment = InventoryFragment()
            }
            val fragmentManager: FragmentManager =
                viewWeakReference!!.get()!!.requireActivity().supportFragmentManager
            val tag: String = InventoryFragment::class.java.simpleName
            fragmentManager.beginTransaction()
                .add(R.id.activity_main_default_fragment_holder, inventoryFragment!!, tag)
                .addToBackStack(tag)
                .commit()
        }
    }

    override fun gotoGroceryView() {
        kotlin.runCatching {
            if (groceryFragment == null) {
                groceryFragment = GroceryFragment()
            }
            val fragmentManager: FragmentManager =
                viewWeakReference!!.get()!!.requireActivity().supportFragmentManager
            val tag: String = GroceryFragment::class.java.simpleName
            fragmentManager.beginTransaction()
                .add(R.id.activity_main_default_fragment_holder, groceryFragment!!, tag)
                .addToBackStack(tag)
                .commit()
        }
    }

}