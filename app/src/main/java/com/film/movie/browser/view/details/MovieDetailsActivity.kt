package com.film.movie.browser.view.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.film.movie.browser.R
import com.film.movie.browser.databinding.ActivityMovieDetailsBinding
import com.film.movie.browser.repository.MovieRepository

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1)
        val movie = MovieRepository.getMovieById(movieId)

        if (movie != null) {
            binding.imagePoster.load(movie.posterUrl)
            binding.textTitle.text = movie.title
            binding.textYear.text = movie.year
            binding.textOverview.text = movie.overview
            updateFavoriteButton(movie.isFavorite)

            binding.buttonToggleFavorite.setOnClickListener {
                val updated = MovieRepository.toggleFavorite(movie.id)
                if (updated != null) {
                    updateFavoriteButton(updated.isFavorite)
                }
            }
        } else {
            finish()
        }
    }

    private fun updateFavoriteButton(isFavorite: Boolean) {
        binding.buttonToggleFavorite.text = if (isFavorite) {
            getString(R.string.action_remove_from_favorites)
        } else {
            getString(R.string.action_add_to_favorites)
        }
    }

    companion object {
        const val EXTRA_MOVIE_ID = "extra_movie_id"
    }
}