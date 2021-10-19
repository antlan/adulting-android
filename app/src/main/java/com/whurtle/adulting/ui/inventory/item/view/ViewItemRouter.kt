package com.whurtle.adulting.ui.inventory.item.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import timber.log.Timber
import java.lang.ref.WeakReference

interface IViewItemRouter {
    fun setWeakReference(reference: Fragment)
    fun goBack()
}

class ViewItemRouter : IViewItemRouter {

    var viewWeakReference: WeakReference<Fragment>? = null

    override fun setWeakReference(reference: Fragment) {
        viewWeakReference = WeakReference(reference)
    }

    override fun goBack() {
        val fragmentManager: FragmentManager =
            viewWeakReference!!.get()!!.requireActivity().supportFragmentManager
        Timber.d("backstack count ${fragmentManager.backStackEntryCount}")
        fragmentManager.popBackStack()
    }
}