package io.damo.chucknorrisjokes

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import io.damo.chucknorrisjokes.extensions.bindView
import io.damo.chucknorrisjokes.extensions.observe
import io.damo.chucknorrisjokes.icndb.IcndbApi
import io.damo.chucknorrisjokes.icndb.Result.Error
import io.damo.chucknorrisjokes.icndb.Result.Success
import rx.Subscription

class MainActivity : AppCompatActivity() {

    private lateinit var api: IcndbApi
    private lateinit var subscription: Subscription

    private val joke: TextView by bindView(R.id.random_joke)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)
        api = serviceLocator.api
    }

    override fun onResume() {
        super.onResume()

        subscription = observe { api.fetchRandomJoke() }
            .subscribe { result ->
                when (result) {
                    is Success ->
                        joke.text = result.value.text

                    is Error ->
                        joke.text = result.message
                }
            }
    }

    override fun onPause() {
        subscription.unsubscribe()
        super.onPause()
    }
}
