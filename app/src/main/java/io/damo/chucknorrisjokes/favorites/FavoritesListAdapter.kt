package io.damo.chucknorrisjokes.favorites

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import io.damo.chucknorrisjokes.R
import io.damo.chucknorrisjokes.icndb.Joke

class FavoritesListAdapter(context: Context) : BaseAdapter() {

    private val layoutInflater = LayoutInflater.from(context)

    var favoriteJokes: List<Joke> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun getItem(position: Int) = favoriteJokes[position]

    override fun getItemId(position: Int) = favoriteJokes[position].id.toLong()

    override fun getCount() = favoriteJokes.size

    override fun getView(position: Int, reuseView: View?, parent: ViewGroup?): View {
        val view = reuseView ?: inflateNewView(parent)
        val viewHolder = view.tag as JokeViewHolder

        viewHolder.joke.text = favoriteJokes[position].text

        return view
    }


    private fun inflateNewView(parent: ViewGroup?): View {
        val view = layoutInflater.inflate(R.layout.joke, parent, false)
        view.tag = JokeViewHolder(view)
        return view
    }

    private class JokeViewHolder(view: View) {
        val joke = view.findViewById(R.id.joke) as TextView
    }
}
