package com.whurtle.adulting.ui.main

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.whurtle.adulting.R
import com.whurtle.adulting.ui.dashboard.DashboardFragment
import timber.log.Timber
import java.lang.ref.WeakReference

interface IMainRouter {
    fun setWeakReference(reference: AppCompatActivity)
    fun loadDefaultFragment()
}

class MainRouter : IMainRouter {

    var defaultFragment: Fragment? = null
    var viewWeakReference: WeakReference<AppCompatActivity>? = null

    override fun setWeakReference(reference: AppCompatActivity) {
        viewWeakReference = WeakReference(reference)
    }

    override fun loadDefaultFragment() {
        val fragmentManager: FragmentManager = viewWeakReference!!.get()!!.supportFragmentManager
        val tag = "fragment_default"
        if (defaultFragment == null) {
            defaultFragment = DashboardFragment()
        }
        var backstackCount = fragmentManager.backStackEntryCount
        while (backstackCount > 0) {
            fragmentManager.popBackStack()
            backstackCount--
        }

        fragmentManager.addOnBackStackChangedListener {
            val f = fragmentManager.fragments[fragmentManager.backStackEntryCount]
            f.onResume()
        }

        fragmentManager.beginTransaction()
            .replace(
                R.id.activity_main_default_fragment_holder,
                (defaultFragment)!!, tag
            )
            .commit()
        Timber.d("Loaded default fragment")
    }
}