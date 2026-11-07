package ar.edu.unicen.seminarioandroid.data


// el repository es la unica capa que conoce de donde salen los datos
// el viewmodel le pide peliculas a el y no sabe si vienen de retrofit, de una base de datos o de donde sea
// recibe la api por parametro (con un valor por defecto) para que sea facil de cambiar o testear
class MovieRepository(
    private val api: TmdbApi = RetrofitClient.api
) {

    // devuelve la lista de peliculas populares
    // la api devuelve un objeto con la pagina y los results, aca nos quedamos solo con la lista
    suspend fun getPopularMovies(): List<Movie> {
        return api.getPopular().results
    }

    // devuelve el detalle de una pelicula segun su id
    suspend fun getMovieDetails(id: Int): MovieDetails {
        return api.getDetails(id)
    }
}