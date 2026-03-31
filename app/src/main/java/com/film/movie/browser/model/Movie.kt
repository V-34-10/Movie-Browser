package com.film.movie.browser.model

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val year: String,
    var isFavorite: Boolean = false
)