package io.damo.chucknorrisjokes

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.main.*

class MainActivity : AppCompatActivity() {


    private lateinit var randomJokeFragment: Fragment
    private lateinit var categoriesFragment: Fragment
    private lateinit var favoritesFragment: Fragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)

        randomJokeFragment = RandomJokeFragment()
        categoriesFragment = CategoriesFragment()
        favoritesFragment = FavoritesFragment()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment, randomJokeFragment)
            .commit()

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.random -> {
                    navigateToRandom()
                    true
                }

                R.id.categories -> {
                    navigateToCategories()
                    true
                }

                R.id.favorites -> {
                    navigateToFavorites()
                    true
                }

                else -> false
            }
        }
    }


    private fun navigateToCategories() = navigateTo(categoriesFragment, R.string.categories)

    private fun navigateToRandom() = navigateTo(randomJokeFragment, R.string.app_label)

    private fun navigateToFavorites() = navigateTo(favoritesFragment, R.string.favorites)

    private fun navigateTo(fragment: Fragment, @StringRes resId: Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment, fragment)
            .commit()
        setTitle(resId)
    }
}
