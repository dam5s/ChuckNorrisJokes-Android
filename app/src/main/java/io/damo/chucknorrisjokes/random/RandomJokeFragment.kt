package io.damo.chucknorrisjokes.random

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.damo.chucknorrisjokes.R
import io.damo.chucknorrisjokes.favorites.Favorites
import io.damo.chucknorrisjokes.icndb.Joke
import io.damo.chucknorrisjokes.serviceLocator
import io.damo.chucknorrisjokes.utils.highlightName
import kotlinx.android.synthetic.main.random_joke.*

class RandomJokeFragment : Fragment() {

    companion object {
        fun build(joke: Joke): RandomJokeFragment {
            val bundle = Bundle().apply {
                putSerializable("joke", joke)
            }

            return RandomJokeFragment().apply { arguments = bundle }
        }
    }

    private lateinit var favorites: Favorites
    private lateinit var joke: Joke

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favorites = serviceLocator.favorites
        joke = arguments.getSerializable("joke") as Joke
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater.inflate(R.layout.random_joke, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        randomJoke.text = highlightName(joke.text)
    }
}
