package io.damo.chucknorrisjokes.favorites

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.damo.chucknorrisjokes.R
import io.damo.chucknorrisjokes.icndb.Joke
import io.damo.chucknorrisjokes.utils.highlightName

class FavoritesAdapter(val jokes: List<Joke>, val context: Context) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)


    override fun getItemCount() = jokes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.joke.text = context.highlightName(jokes[position].text)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.joke, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val joke = view.findViewById(R.id.joke) as TextView
    }
}
