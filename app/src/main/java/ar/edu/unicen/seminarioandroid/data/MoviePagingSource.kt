package ar.edu.unicen.seminarioandroid.data


import androidx.paging.PagingSource
import androidx.paging.PagingState

// le dice a paging como cargar cada pagina de peliculas populares
// la key es el numero de pagina
class MoviePagingSource(private val api: TmdbApi) : PagingSource<Int, Movie>() {

    // paging llama a esta funcion cada vez que necesita una pagina nueva
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        // la primera vez params.key es null, arrancamos en la pagina 1
        val page = params.key ?: 1
        return try {
            val response = api.getPopular(page)
            LoadResult.Page(
                data = response.results,
                // en la pagina 1 no hay anterior
                prevKey = if (page == 1) null else page - 1,
                // si ya estamos en la ultima pagina no hay siguiente y paging deja de pedir
                nextKey = if (page >= response.totalPages) null else page + 1
            )
        } catch (e: Exception) {
            // devolvemos el error como resultado en vez de lanzarlo, paging lo expone en el loadstate
            LoadResult.Error(e)
        }
    }

    // si hay que recargar, vuelve a la pagina mas cercana a lo que el usuario estaba viendo
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.let { it.prevKey?.plus(1) ?: it.nextKey?.minus(1) }
        }
    }
}