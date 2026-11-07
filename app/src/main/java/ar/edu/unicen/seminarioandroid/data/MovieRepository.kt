package ar.edu.unicen.seminarioandroid.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow


// el repository es la unica capa que conoce de donde salen los datos
// el viewmodel le pide peliculas a el y no sabe si vienen de retrofit, de una base de datos o de donde sea
// recibe la api por parametro (con un valor por defecto) para que sea facil de cambiar o testear
class MovieRepository(
    private val api: TmdbApi = RetrofitClient.api
) {


    //devuelve un flow de pagingdata en vez de una lista
    // el pager arma las paginas usando el pagingsource y pide mas cuando el usuario llega al final
    fun getPopularMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                // carga solo la primera pagina al inicio
                initialLoadSize = 20,
                // pide la siguiente cuando faltan 2 items para el final (por defecto 20, no se veia la paginacion porque cargaban antes de que llegue al final)
                prefetchDistance = 2,
                enablePlaceholders = false
            )
        ) {
            MoviePagingSource(api)
        }.flow
    }

    // devuelve el detalle de una pelicula segun su id
    suspend fun getMovieDetails(id: Int): MovieDetails {
        return api.getDetails(id)
    }
}