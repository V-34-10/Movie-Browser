package com.film.movie.browser.view.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.film.movie.browser.adapters.MovieAdapter
import com.film.movie.browser.databinding.FragmentFavoritesBinding
import com.film.movie.browser.repository.MovieRepository
import com.film.movie.browser.view.details.MovieDetailsActivity

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
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
                val favorites = MovieRepository.getFavorites()
                adapter.submitList(
                    favorites.map {
                        if (it.id == movie.id && updated != null) updated else it
                    }
                )
            }
        )

        binding.recyclerFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFavorites.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        adapter.submitList(MovieRepository.getFavorites())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}