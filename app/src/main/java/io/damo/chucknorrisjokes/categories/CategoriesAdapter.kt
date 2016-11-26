package io.damo.chucknorrisjokes.categories

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
import io.damo.chucknorrisjokes.icndb.Category

class CategoriesAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    var categories: List<Category> = emptyList()
        set(categories) {
            field = categories
            notifyDataSetChanged()
        }


    override fun getItem(position: Int)
        = CategoryJokesFragment.build(categories[position].name)

    override fun getCount()
        = categories.size

    override fun getPageTitle(position: Int)
        = categories[position].name


    /*
     * Fix an issue with FragmentPagerAdapter:
     *
     * When using the adapter with a different ViewPager (same id),
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
