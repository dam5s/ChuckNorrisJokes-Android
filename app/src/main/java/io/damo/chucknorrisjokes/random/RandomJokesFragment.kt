package io.damo.chucknorrisjokes.random

import android.content.res.ColorStateList
import android.graphics.PorterDuff.Mode.MULTIPLY
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v4.view.ViewPager.OnPageChangeListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.damo.chucknorrisjokes.R
import io.damo.chucknorrisjokes.favorites.Favorites
import io.damo.chucknorrisjokes.icndb.IcndbApi
import io.damo.chucknorrisjokes.icndb.Joke
import io.damo.chucknorrisjokes.serviceLocator
import io.damo.chucknorrisjokes.utils.color
import io.damo.chucknorrisjokes.utils.observe
import io.damo.chucknorrisjokes.utils.toast
import kotlinx.android.synthetic.main.random_jokes.*
import rx.Subscription


class RandomJokesFragment : Fragment(), OnPageChangeListener {

    private lateinit var api: IcndbApi
    private lateinit var favorites: Favorites

    private var adapter: RandomJokesAdapter? = null
    private var subscription: Subscription? = null

    private val currentJoke: Joke
        get() = adapter!!.jokes[randomJokesViewPager.currentItem]


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favorites = serviceLocator.favorites
        api = serviceLocator.api

        if (adapter == null) {
            adapter = RandomJokesAdapter(fragmentManager)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater.inflate(R.layout.random_jokes, container, false)


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        randomJokesViewPager.adapter = adapter
        randomJokesViewPager.addOnPageChangeListener(this)

        addToFavorites.apply {
            setColors(R.color.windowBg, R.color.accent)
            setOnClickListener { currentJoke.addToFavorites() }
        }

        removeFromFavorites.apply {
            setColors(R.color.windowBg, R.color.accent)
            setOnClickListener { currentJoke.removeFromFavorites() }
        }

        loadMoreJokes()
    }

    override fun onDestroyView() {
        subscription?.unsubscribe()
        super.onDestroyView()
    }


    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        displayFavoriteButtons()

        if (adapter!!.count - position <= 2) {
            loadMoreJokes()
        }
    }


    private fun Joke.addToFavorites() {
        addToFavorites.hide()
        favorites.add(this)
        displayFavoriteButtons()
    }

    private fun Joke.removeFromFavorites() {
        removeFromFavorites.hide()
        favorites.remove(this)
        displayFavoriteButtons()
    }

    private fun FloatingActionButton.setColors(backgroundColorId: Int, iconColorId: Int) {
        val updatedDrawable = drawable.copyWithTint(color(iconColorId))

        backgroundTintList = ColorStateList.valueOf(color(backgroundColorId))
        setImageDrawable(updatedDrawable)
    }

    private fun displayFavoriteButtons() {
        val canAdd = favorites.canAdd(currentJoke)
        addToFavorites.showIf(canAdd)
        removeFromFavorites.showIf(!canAdd)
    }

    fun FloatingActionButton.showIf(condition: Boolean) {
        if (condition) show() else hide()
    }

    private fun Drawable.copyWithTint(color: Int) = constantState
        .newDrawable()
        .mutate()
        .apply { setColorFilter(color, MULTIPLY) }

    private fun loadMoreJokes() {
        subscription?.unsubscribe()

        subscription = observe { api.fetchRandomJokes() }
            .subscribe { result ->
                result
                    .then {
                        adapter?.jokes?.addAll(it)
                        adapter?.notifyDataSetChanged()
                        displayFavoriteButtons()
                    }
                    .otherwise { toast(it) }
            }
    }
}
