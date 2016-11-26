package io.damo.chucknorrisjokes.random

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import io.damo.chucknorrisjokes.R
import io.damo.chucknorrisjokes.favorites.Favorites
import io.damo.chucknorrisjokes.icndb.IcndbApi
import io.damo.chucknorrisjokes.icndb.Joke
import io.damo.chucknorrisjokes.serviceLocator
import io.damo.chucknorrisjokes.utils.Result
import io.damo.chucknorrisjokes.utils.Result.Success
import io.damo.chucknorrisjokes.utils.observe
import io.damo.chucknorrisjokes.utils.toast
import kotlinx.android.synthetic.main.random_joke.*
import rx.Subscription


class RandomJokeFragment : Fragment() {

    private lateinit var api: IcndbApi
    private lateinit var favorites: Favorites
    private lateinit var subscription: Subscription

    private var joke: Joke? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater.inflate(R.layout.random_joke, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = serviceLocator.api
        favorites = serviceLocator.favorites
    }

    override fun onResume() {
        super.onResume()

        subscription = observe { api.fetchRandomJoke() }
            .subscribe { result ->
                when (result) {
                    is Success -> {
                        joke = result.value

                        randomJoke.text = joke!!.text
                        addToFavorites.visibility = visibleIf(favorites.canAdd(joke!!))
                    }


                    is Result.Error ->
                        toast(result.message)
                }
            }

        addToFavorites.setOnClickListener {
            joke?.let {
                when (favorites.add(it)) {
                    is Success ->
                        addToFavorites.visibility = GONE
                }
            }
        }
    }

    fun visibleIf(condition: Boolean, otherwise: Int = GONE)
        = if (condition) VISIBLE else otherwise

    override fun onPause() {
        subscription.unsubscribe()
        super.onPause()
    }
}
