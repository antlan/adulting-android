package com.whurtle.adulting.ui.inventory.item.create

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.whurtle.adulting.R
import timber.log.Timber
import java.lang.ref.WeakReference

interface ICreateItemRouter {
    fun setWeakReference(reference: Fragment)
    fun goBack()
}

class CreateItemRouter : ICreateItemRouter {

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