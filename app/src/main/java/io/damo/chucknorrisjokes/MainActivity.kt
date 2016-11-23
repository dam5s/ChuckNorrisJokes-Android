package io.damo.chucknorrisjokes

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment, RandomJokeFragment(), "random_joke")
            .commit()
    }
}
