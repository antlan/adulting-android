package com.whurtle.adulting.ui.grocery

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.whurtle.adulting.R
import com.whurtle.adulting.store.grocery.GroceryItem
import com.whurtle.adulting.store.grocery.GroceryItemFull
import com.whurtle.adulting.store.inventory.Item
import com.whurtle.adulting.ui.inventory.item.create.CreateItemFragment
import com.whurtle.adulting.ui.inventory.item.view.ViewItemFragment
import java.lang.ref.WeakReference

interface IGroceryRouter {
    fun setWeakReference(reference: Fragment)
    fun gotoCreateItemView()
    fun gotoItemView(item: GroceryItemFull)

}

class GroceryRouter : IGroceryRouter {

    var createItemFragment: CreateItemFragment? = null

    var viewWeakReference: WeakReference<Fragment>? = null

    override fun setWeakReference(reference: Fragment) {
        viewWeakReference = WeakReference(reference)
    }

    override fun gotoCreateItemView() {
        if (createItemFragment == null) {
            createItemFragment = CreateItemFragment()
        }
        val fragmentManager: FragmentManager =
            viewWeakReference!!.get()!!.requireActivity().supportFragmentManager
        val tag: String = CreateItemFragment::class.java.getSimpleName()
        fragmentManager.beginTransaction()
            .add(R.id.activity_main_default_fragment_holder, createItemFragment!!, tag)
            .addToBackStack(tag)
            .commit()
    }

    override fun gotoItemView(item: GroceryItemFull) {
//
//        val fragment = ViewItemFragment.newInstance(item)
//        val fragmentManager: FragmentManager =
//            viewWeakReference!!.get()!!.requireActivity().supportFragmentManager
//        val tag: String = ViewItemFragment::class.java.getSimpleName()
//        fragmentManager.beginTransaction()
//            .add(R.id.activity_main_default_fragment_holder, fragment, tag)
//            .addToBackStack(tag)
//            .commit()
    }
}