package io.damo.chucknorrisjokes.categories

import android.support.v4.app.FragmentManager
import io.damo.chucknorrisjokes.icndb.Category
import io.damo.chucknorrisjokes.utils.InsuredParentViewFragmentPagerAdapter

class CategoriesAdapter(fragmentManager: FragmentManager) : InsuredParentViewFragmentPagerAdapter(fragmentManager) {

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
}
