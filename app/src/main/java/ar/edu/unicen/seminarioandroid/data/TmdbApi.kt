package ar.edu.unicen.seminarioandroid.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// interfaz que describe los endpoints de tmdb, retrofit genera la implementacion solo
interface TmdbApi {

    // devuelve la lista de peliculas populares (pagina 1 por defecto)
    // es suspend para poder llamarla desde una corrutina sin bloquear el hilo principal
    @GET("movie/popular")
    suspend fun getPopular(@Query("page") page: Int = 1): MovieListResponse

    // devuelve el detalle de una pelicula, el id se reemplaza en la url donde esta {id}
    @GET("movie/{id}")
    suspend fun getDetails(@Path("id") id: Int): MovieDetails
}