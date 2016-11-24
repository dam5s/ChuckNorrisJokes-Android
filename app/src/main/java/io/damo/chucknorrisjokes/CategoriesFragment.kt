package io.damo.chucknorrisjokes

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.damo.chucknorrisjokes.extensions.observe
import io.damo.chucknorrisjokes.icndb.Category
import io.damo.chucknorrisjokes.icndb.Result.Success
import kotlinx.android.synthetic.main.categories.*
import rx.Subscription

class CategoriesFragment : Fragment() {

    private lateinit var subscription: Subscription
    private lateinit var adapter: CategoriesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = CategoriesAdapter(fragmentManager)
        subscription = observe { serviceLocator.api.fetchCategories() }
            .subscribe { result ->
                when (result) {
                    is Success -> {
                        adapter.categories = addNone(result.value)
                        setupTabs()
                    }
                }
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater.inflate(R.layout.categories, container, false)

    override fun onResume() {
        super.onResume()
        setupTabs()
    }

    override fun onDestroy() {
        subscription.unsubscribe()
        super.onDestroy()
    }


    private fun setupTabs() {
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun addNone(categories: List<Category>): List<Category> {
        val none = Category(getString(R.string.none))

        return categories.toMutableList().apply { add(0, none) }
    }
}
