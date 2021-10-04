package com.whurtle.adulting.ui.inventory.item.create

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.whurtle.adulting.R
import java.lang.ref.WeakReference

interface ICreateItemRouter {
    fun setWeakReference(reference: Fragment)
    fun gotoInventoryView()
}

class CreateItemRouter : ICreateItemRouter {

    var createItemFragment: CreateItemFragment? = null

    var viewWeakReference: WeakReference<Fragment>? = null

    override fun setWeakReference(reference: Fragment) {
        viewWeakReference = WeakReference(reference)
    }

    override fun gotoInventoryView() {
        if (createItemFragment == null) {
            createItemFragment = CreateItemFragment()
        }
        val fragmentManager: FragmentManager =
            viewWeakReference!!.get()!!.requireActivity().supportFragmentManager
        val tag: String = CreateItemFragment::class.java.getSimpleName()
        fragmentManager.beginTransaction()
            .replace(R.id.activity_main_default_fragment_holder, createItemFragment!!, tag)
            .commit()
    }
}