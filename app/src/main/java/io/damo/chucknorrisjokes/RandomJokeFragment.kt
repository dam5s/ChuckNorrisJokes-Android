package io.damo.chucknorrisjokes

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import io.damo.chucknorrisjokes.extensions.bindView
import io.damo.chucknorrisjokes.extensions.observe
import io.damo.chucknorrisjokes.extensions.toast
import io.damo.chucknorrisjokes.icndb.IcndbApi
import io.damo.chucknorrisjokes.icndb.Result
import rx.Subscription


class RandomJokeFragment : Fragment() {

    private lateinit var api: IcndbApi
    private lateinit var subscription: Subscription

    private val jokeTextView: TextView by bindView(R.id.random_joke)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
        = inflater.inflate(R.layout.random_joke, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = serviceLocator.api
    }

    override fun onResume() {
        super.onResume()

        subscription = observe { api.fetchRandomJoke() }
            .subscribe { result ->
                when (result) {
                    is Result.Success ->
                        jokeTextView.text = result.value.text

                    is Result.Error ->
                        toast(result.message)
                }
            }
    }

    override fun onPause() {
        subscription.unsubscribe()
        super.onPause()
    }
}
