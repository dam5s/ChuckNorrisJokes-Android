package io.damo.chucknorrisjokes.utils

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup

abstract class InsuredParentViewFragmentPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    /*
     * Fix an issue with FragmentPagerAdapter:
     *
     * When using the adapter with a different ViewPager (but with same id),
     * It does not ensure the fragment's view is in the new ViewPager.
     */
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment

        fragment.view?.let { view ->
            if (view.parent != container) {
                (view.parent as? ViewGroup)?.removeView(view)
                container.addView(view)
            }
        }

        return fragment
    }
}
