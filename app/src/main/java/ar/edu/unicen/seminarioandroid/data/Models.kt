package ar.edu.unicen.seminarioandroid.data

import com.google.gson.annotations.SerializedName

// respuesta de la api cuando pedimos una lista de peliculas (populares, busqueda, etc)
data class MovieListResponse(
    val page: Int,
    val results: List<Movie>,
    // lo necesitamos para saber cuando no hay mas paginas
    @SerializedName("total_pages") val totalPages: Int
)

// version resumida de una pelicula, la que se usa en las listas
// posterPath puede venir null si la pelicula no tiene imagen
data class Movie(
    val id: Int,
    val title: String,
    // la api manda "poster_path" y en kotlin usamos camelcase, por eso la anotacion
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("vote_average") val voteAverage: Double
)

data class Genre(val id: Int, val name: String)

// datos completos de una pelicula, los usamos en la pantalla de detalle
data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    val genres: List<Genre>,
    // no todas las peliculas tienen homepage, la dejamos nullable para el desafio h
    val homepage: String?
)