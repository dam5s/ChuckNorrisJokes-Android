package io.damo.chucknorrisjokes.favorites

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import io.damo.chucknorrisjokes.R
import io.damo.chucknorrisjokes.serviceLocator
import kotlinx.android.synthetic.main.favorites.*

class FavoritesFragment : Fragment() {

    lateinit var favorites: Favorites
    lateinit var favoritesListAdapter: FavoritesListAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater.inflate(R.layout.favorites, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        favorites = serviceLocator.favorites
        favoritesListAdapter = FavoritesListAdapter(context)
    }

    override fun onResume() {
        super.onResume()

        if (favoritesListView.adapter == null) {
            favoritesListView.adapter = favoritesListAdapter
        }

        val jokes = favorites.all()

        if (jokes.isEmpty()) {
            nothingInFavorites.visibility = VISIBLE
            favoritesListView.visibility = GONE
        } else {
            nothingInFavorites.visibility = GONE
            favoritesListView.visibility = VISIBLE
        }

        favoritesListAdapter.favoriteJokes = jokes
    }
}
