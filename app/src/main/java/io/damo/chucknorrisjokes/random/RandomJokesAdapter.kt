package io.damo.chucknorrisjokes.random

import android.support.v4.app.FragmentManager
import io.damo.chucknorrisjokes.icndb.Joke
import io.damo.chucknorrisjokes.utils.InsuredParentViewFragmentPagerAdapter

class RandomJokesAdapter(fragmentManager: FragmentManager) : InsuredParentViewFragmentPagerAdapter(fragmentManager) {

    val jokes = arrayListOf<Joke>()


    override fun getItem(position: Int)
        = RandomJokeFragment.build(jokes[position])

    override fun getCount()
        = jokes.size

    override fun getPageTitle(position: Int)
        = "Joke"
}
