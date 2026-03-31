package com.film.movie.browser.repository

import com.film.movie.browser.model.Movie
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val TMDB_API_BASE_URL = "https://api.themoviedb.org/3/"
private const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

data class TmdbMovieDto(
    val id: Int,
    val title: String?,
    val name: String?,
    val overview: String?,
    val poster_path: String?,
    val release_date: String?
)

data class TmdbMoviesResponse(
    val results: List<TmdbMovieDto>
)

interface TmdbApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): TmdbMoviesResponse
}

object TmdbApi {
    val service: TmdbApiService by lazy {
        Retrofit.Builder()
            .baseUrl(TMDB_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmdbApiService::class.java)
    }

    fun mapDtoToMovie(dto: TmdbMovieDto, isFavorite: Boolean): Movie {
        val title = dto.title ?: dto.name ?: "Untitled"
        val overview = dto.overview ?: ""
        val posterUrl = dto.poster_path?.let { "$TMDB_IMAGE_BASE_URL$it" } ?: ""
        val year = dto.release_date?.take(4) ?: ""
        return Movie(
            id = dto.id,
            title = title,
            overview = overview,
            posterUrl = posterUrl,
            year = year,
            isFavorite = isFavorite
        )
    }
}

