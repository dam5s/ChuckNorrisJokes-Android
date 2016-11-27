package io.damo.chucknorrisjokes.favorites

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.helper.ItemTouchHelper.RIGHT
import android.support.v7.widget.helper.ItemTouchHelper.SimpleCallback
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.damo.chucknorrisjokes.R
import io.damo.chucknorrisjokes.serviceLocator
import io.damo.chucknorrisjokes.utils.setVisibleIf
import io.damo.chucknorrisjokes.utils.snackbar
import kotlinx.android.synthetic.main.favorites.*


class FavoritesFragment : Fragment() {

    private lateinit var favorites: Favorites
    private lateinit var adapter: FavoritesAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        favorites = serviceLocator.favorites
        adapter = FavoritesAdapter(favorites.all(), context)
        layoutManager = LinearLayoutManager(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater.inflate(R.layout.favorites, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        favoritesView.layoutManager = layoutManager
        favoritesView.adapter = adapter
        ItemTouchHelper(favoritesCallback).attachToRecyclerView(favoritesView)
    }

    override fun onResume() {
        super.onResume()

        updateViewsVisibility()
        adapter.notifyDataSetChanged()
    }

    private val favoritesCallback = object : SimpleCallback(RIGHT, RIGHT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder)
            = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val joke = adapter.jokes[position]

            favorites.remove(joke)
            adapter.notifyItemRemoved(position)

            updateViewsVisibility()

            context
                .snackbar(favoritesView, R.string.removed_from_favorites)
                .setAction(R.string.undo) {
                    favorites.add(joke).then {
                        adapter.notifyItemInserted(it)
                        updateViewsVisibility()
                    }
                }
                .show()
        }
    }

    private fun updateViewsVisibility() {
        nothingInFavorites.setVisibleIf(adapter.jokes.isEmpty())
        favoritesView.setVisibleIf(adapter.jokes.isNotEmpty())
    }
}
