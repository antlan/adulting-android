package com.whurtle.adulting.ui.dashboard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.whurtle.adulting.R
import com.whurtle.adulting.ui.inventory.InventoryFragment
import java.lang.ref.WeakReference

interface IDashboardRouter {
    fun setWeakReference(reference: Fragment)
    fun gotoInventoryView()
}


class DashboardRouter : IDashboardRouter {

    var inventoryFragment: InventoryFragment? = null

    var viewWeakReference: WeakReference<Fragment>? = null

    override fun setWeakReference(reference: Fragment) {
        viewWeakReference = WeakReference(reference)
    }

    override fun gotoInventoryView() {
        if (inventoryFragment == null) {
            inventoryFragment = InventoryFragment()
        }
        val fragmentManager: FragmentManager =
            viewWeakReference!!.get()!!.requireActivity().supportFragmentManager
        val tag: String = InventoryFragment::class.java.getSimpleName()
        fragmentManager.beginTransaction()
            .replace(R.id.activity_main_default_fragment_holder, inventoryFragment!!, tag)
            .commit()
    }
}