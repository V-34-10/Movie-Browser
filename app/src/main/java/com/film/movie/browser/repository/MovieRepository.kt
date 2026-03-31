package com.film.movie.browser.repository

import com.film.movie.browser.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MovieRepository {

    private val favoritesIds = mutableSetOf<Int>()
    private var cachedMovies: List<Movie> = emptyList()

    suspend fun loadMovies(apiKey: String): List<Movie> = withContext(Dispatchers.IO) {
        val response = TmdbApi.service.getPopularMovies(apiKey = apiKey)
        val movies = response.results.map { dto ->
            val isFavorite = favoritesIds.contains(dto.id)
            TmdbApi.mapDtoToMovie(dto, isFavorite)
        }
        cachedMovies = movies
        getCachedMovies()
    }

    fun getCachedMovies(): List<Movie> =
        cachedMovies.map { movie ->
            movie.copy(isFavorite = favoritesIds.contains(movie.id))
        }

    fun getFavorites(): List<Movie> =
        getCachedMovies().filter { it.isFavorite }

    fun getMovieById(id: Int): Movie? =
        getCachedMovies().find { it.id == id }

    fun toggleFavorite(id: Int): Movie? {
        if (favoritesIds.contains(id)) {
            favoritesIds.remove(id)
        } else {
            favoritesIds.add(id)
        }
        val movie = cachedMovies.find { it.id == id }
        if (movie != null) {
            val updated = movie.copy(isFavorite = favoritesIds.contains(id))
            cachedMovies = cachedMovies.map {
                if (it.id == id) updated else it
            }
            return updated
        }
        return null
    }
}