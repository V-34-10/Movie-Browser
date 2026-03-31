package com.film.movie.browser.view.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.film.movie.browser.BuildConfig
import com.film.movie.browser.adapters.MovieAdapter
import com.film.movie.browser.databinding.FragmentMoviesListBinding
import com.film.movie.browser.repository.MovieRepository
import com.film.movie.browser.view.details.MovieDetailsActivity
import kotlinx.coroutines.launch

class MoviesListFragment : Fragment() {

    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MovieAdapter(
            onItemClick = { movie ->
                val intent = Intent(requireContext(), MovieDetailsActivity::class.java)
                intent.putExtra(MovieDetailsActivity.Companion.EXTRA_MOVIE_ID, movie.id)
                startActivity(intent)
            },
            onFavoriteClick = { movie ->
                val updated = MovieRepository.toggleFavorite(movie.id)
                val current = MovieRepository.getCachedMovies()
                adapter.submitList(
                    current.map {
                        if (it.id == movie.id && updated != null) updated else it
                    }
                )
            }
        )

        binding.recyclerMovies.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMovies.adapter = adapter

        loadMovies()
    }

    private fun loadMovies() {
        val apiKey = BuildConfig.TMDB_API_KEY
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val movies = MovieRepository.loadMovies(apiKey)
                adapter.submitList(movies)
            } catch (e: Exception) {
            }
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.submitList(MovieRepository.getCachedMovies())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}