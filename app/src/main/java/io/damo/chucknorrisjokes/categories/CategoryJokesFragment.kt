package io.damo.chucknorrisjokes.categories

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.damo.chucknorrisjokes.R
import io.damo.chucknorrisjokes.icndb.Joke
import io.damo.chucknorrisjokes.serviceLocator
import io.damo.chucknorrisjokes.utils.observe
import kotlinx.android.synthetic.main.category_jokes.*
import rx.Subscription

class CategoryJokesFragment : Fragment() {

    companion object {
        fun build(categoryName: String): CategoryJokesFragment {
            val bundle = Bundle().apply {
                putString("categoryName", categoryName)
            }

            return CategoryJokesFragment().apply { arguments = bundle }
        }
    }


    private lateinit var subscription: Subscription
    private lateinit var jokes: List<Joke>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val categoryName = arguments["categoryName"] as String
        val api = serviceLocator.api

        jokes = emptyList()
        subscription = observe { api.fetchCategoryJokes(categoryName) }
            .subscribe { result ->
                result.then {
                    jokes = it
                    displayJokes()
                }
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater.inflate(R.layout.category_jokes, container, false)

    override fun onResume() {
        super.onResume()
        displayJokes()
    }

    override fun onDestroy() {
        subscription.unsubscribe()
        super.onDestroy()
    }


    private fun displayJokes() {
        if (jokes.size == 3) {
            joke1.text = jokes[0].text
            joke2.text = jokes[1].text
            joke3.text = jokes[2].text
        }
    }
}
