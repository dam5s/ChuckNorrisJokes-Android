package io.damo.chucknorrisjokes

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import io.damo.chucknorrisjokes.CategoryJokesFragment.Companion.CATEGORY_NAME
import io.damo.chucknorrisjokes.icndb.Category

class CategoriesAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    var categories: List<Category> = emptyList()
        set(categories) {
            field = categories
            notifyDataSetChanged()
        }


    override fun getItem(position: Int) = buildFragment(position)

    override fun getCount() = categories.size

    override fun getPageTitle(position: Int) = categories[position].name


    fun buildFragment(position: Int): Fragment {
        val category = categories[position]
        val bundle = Bundle().apply { putString(CATEGORY_NAME, category.name) }

        return CategoryJokesFragment().apply { arguments = bundle }
    }
}
