package io.damo.chucknorrisjokes.favorites

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.damo.chucknorrisjokes.R
import io.damo.chucknorrisjokes.serviceLocator
import io.damo.chucknorrisjokes.utils.setVisibleIf
import kotlinx.android.synthetic.main.favorites.*

class FavoritesFragment : Fragment() {

    lateinit var favorites: Favorites

    lateinit var favoritesAdapter: FavoritesAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        favoritesAdapter = FavoritesAdapter(context)
        layoutManager = LinearLayoutManager(context)

        val view = inflater.inflate(R.layout.favorites, container, false)

        val favoritesView = view.findViewById(R.id.favoritesView) as RecyclerView
        favoritesView.layoutManager = layoutManager
        favoritesView.adapter = favoritesAdapter

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        favorites = serviceLocator.favorites
    }

    override fun onResume() {
        super.onResume()

        if (favoritesView.adapter == null) {
        }

        val jokes = favorites.all()

        nothingInFavorites.setVisibleIf(jokes.isEmpty())
        favoritesView.setVisibleIf(jokes.isNotEmpty())

        favoritesAdapter.favoriteJokes = jokes
    }
}
