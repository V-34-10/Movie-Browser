package com.film.movie.browser.view.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.film.movie.browser.R
import com.film.movie.browser.view.home.MoviesListFragment
import com.film.movie.browser.view.favorite.FavoritesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, MoviesListFragment())
                .commit()
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_movies -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, MoviesListFragment())
                        .commit()
                    true
                }
                R.id.nav_favorites -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, FavoritesFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}