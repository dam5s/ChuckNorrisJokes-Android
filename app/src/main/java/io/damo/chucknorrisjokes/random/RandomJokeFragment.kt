package io.damo.chucknorrisjokes.random

import android.content.res.ColorStateList
import android.graphics.PorterDuff.Mode.MULTIPLY
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.damo.chucknorrisjokes.R
import io.damo.chucknorrisjokes.favorites.Favorites
import io.damo.chucknorrisjokes.icndb.IcndbApi
import io.damo.chucknorrisjokes.icndb.Joke
import io.damo.chucknorrisjokes.serviceLocator
import io.damo.chucknorrisjokes.utils.*
import kotlinx.android.synthetic.main.random_joke.*
import rx.Subscription


class RandomJokeFragment : Fragment() {

    private lateinit var api: IcndbApi
    private lateinit var favorites: Favorites

    private var subscription: Subscription? = null
    private var joke: Joke? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = serviceLocator.api
        favorites = serviceLocator.favorites
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater.inflate(R.layout.random_joke, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        swipeToRefresh.setOnRefreshListener { loadJoke() }

        addToFavorites.apply {
            setColors(R.color.windowBg, R.color.text)
            setOnClickListener { joke?.addToFavorites() }
        }

        removeFromFavorites.apply {
            setColors(R.color.windowBg, R.color.accent)
            setOnClickListener { joke?.removeFromFavorites() }
        }
    }

    override fun onResume() {
        super.onResume()
        loadJoke()
    }

    override fun onPause() {
        subscription?.unsubscribe()
        super.onPause()
    }


    private fun Joke.addToFavorites() = favorites
        .add(this)
        .then { displayFavoriteButtons() }

    private fun Joke.removeFromFavorites() {
        favorites.remove(this)
        displayFavoriteButtons()
    }

    private fun FloatingActionButton.setColors(backgroundColorId: Int, iconColorId: Int) {
        val updatedDrawable = drawable.copyWithTint(color(iconColorId))

        backgroundTintList = ColorStateList.valueOf(color(backgroundColorId))
        setImageDrawable(updatedDrawable)
    }

    private fun Drawable.copyWithTint(color: Int)
        = constantState
        .newDrawable()
        .mutate()
        .apply {
            setColorFilter(color, MULTIPLY)
        }

    private fun loadJoke() {
        subscription?.unsubscribe()

        subscription = observe { api.fetchRandomJoke() }
            .subscribe { result ->
                result
                    .then {
                        joke = it
                        randomJoke.text = highlightName(joke!!.text)
                        displayFavoriteButtons()
                    }
                    .otherwise { toast(it) }
                    .always { swipeToRefresh.isRefreshing = false }
            }
    }

    private fun displayFavoriteButtons() {
        val canAdd = favorites.canAdd(joke!!)
        addToFavorites.setVisibleIf(canAdd)
        removeFromFavorites.setVisibleIf(!canAdd)
    }
}
