package io.damo.chucknorrisjokes.categories

import android.graphics.PorterDuff.Mode.MULTIPLY
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.damo.chucknorrisjokes.R
import io.damo.chucknorrisjokes.favorites.Favorites
import io.damo.chucknorrisjokes.icndb.Joke
import io.damo.chucknorrisjokes.serviceLocator
import io.damo.chucknorrisjokes.utils.color
import io.damo.chucknorrisjokes.utils.highlightName
import io.damo.chucknorrisjokes.utils.observe
import io.damo.chucknorrisjokes.utils.setVisibleIf
import kotlinx.android.synthetic.main.category_jokes.*
import rx.Subscription

class CategoryJokesFragment : Fragment(), Favorites.Observer {

    companion object {
        fun build(categoryName: String): CategoryJokesFragment {
            val bundle = Bundle().apply {
                putString("categoryName", categoryName)
            }

            return CategoryJokesFragment().apply { arguments = bundle }
        }
    }


    private lateinit var favorites: Favorites
    private lateinit var subscription: Subscription
    private lateinit var jokes: List<Joke>
    private lateinit var viewHolders: List<ViewHolder>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val categoryName = arguments["categoryName"] as String
        val api = serviceLocator.api

        favorites = serviceLocator.favorites
        jokes = emptyList()
        subscription = observe { api.fetchCategoryJokes(categoryName) }
            .subscribe { result ->
                result.then {
                    jokes = it
                    displayJokes()
                }
            }

        favorites.subscribe(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater.inflate(R.layout.category_jokes, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val accentColor = color(R.color.accent)
        val textColor = color(R.color.text)

        viewHolders = listOf(joke1, joke2, joke3).map {
            ViewHolder(it).apply {
                addView.setColorFilter(textColor, MULTIPLY)
                removeView.setColorFilter(accentColor, MULTIPLY)
            }
        }

        displayJokes()
    }

    override fun onResume() {
        super.onResume()
        displayJokes()
    }

    override fun onDestroy() {
        subscription.unsubscribe()
        favorites.unsubscribe(this)
        super.onDestroy()
    }

    override fun onFavoritesChanged() {
        displayJokes()
    }


    private fun displayJokes() {
        jokes.forEachIndexed { index, joke ->
            displayJoke(joke, viewHolders[index])
        }
    }

    private fun displayJoke(joke: Joke, viewHolder: ViewHolder) {
        val canAdd = favorites.canAdd(joke)

        viewHolder.apply {
            textView.text = highlightName(joke.text)
            addView.setVisibleIf(canAdd)
            removeView.setVisibleIf(!canAdd)

            addView.setOnClickListener { joke.addToFavorites() }
            removeView.setOnClickListener { joke.removeFromFavorites() }
        }
    }


    private fun Joke.addToFavorites() {
        favorites.add(this)
        displayJokes()
    }

    private fun Joke.removeFromFavorites() {
        favorites.remove(this)
        displayJokes()
    }

    private class ViewHolder(view: View) {
        val textView = view.findViewById(R.id.text) as TextView
        val addView = view.findViewById(R.id.add) as ImageView
        val removeView = view.findViewById(R.id.remove) as ImageView
    }
}
