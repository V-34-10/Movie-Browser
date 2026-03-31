package com.film.movie.browser.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.film.movie.browser.R
import com.film.movie.browser.databinding.ItemMovieBinding
import com.film.movie.browser.model.Movie

class MovieAdapter(
    private val onItemClick: (Movie) -> Unit,
    private val onFavoriteClick: (Movie) -> Unit
) : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(
        private val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.textTitle.text = movie.title
            binding.textOverview.text = movie.overview
            binding.imagePoster.load(movie.posterUrl)
            updateFavoriteIcon(movie)

            binding.root.setOnClickListener { onItemClick(movie) }
            binding.buttonFavorite.setOnClickListener {
                onFavoriteClick(movie)
            }
        }

        private fun updateFavoriteIcon(movie: Movie) {
            val iconRes =
                if (movie.isFavorite) R.drawable.ico_heart_on2 else R.drawable.ico_heart_off
            binding.buttonFavorite.setBackgroundResource(iconRes)
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem == newItem
    }
}