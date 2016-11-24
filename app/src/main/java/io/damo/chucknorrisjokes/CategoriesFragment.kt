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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater.inflate(R.layout.categories, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subscription = observe { serviceLocator.api.fetchCategories() }
            .subscribe { result ->
                when (result) {
                    is Success ->
                        addTabs(result.value)
                }
            }
    }

    override fun onDestroy() {
        subscription.unsubscribe()
        super.onDestroy()
    }


    private fun addTabs(categories: List<Category>) {
        categories.forEach { addTab(it.name) }
        addTab("None")
    }

    private fun addTab(name: String) {
        val tab = tabLayout.newTab().apply {
            text = name.capitalize()
        }
        tabLayout.addTab(tab)
    }
}
