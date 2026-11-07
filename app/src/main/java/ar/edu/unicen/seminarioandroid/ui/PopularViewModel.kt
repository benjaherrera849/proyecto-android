package ar.edu.unicen.seminarioandroid.ui

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.lifecycle.viewModelScope
import ar.edu.unicen.seminarioandroid.data.Movie
import ar.edu.unicen.seminarioandroid.data.MovieRepository
import kotlinx.coroutines.flow.Flow

// con paging ya no manejamos loading/success/error a mano, lo maneja la libreria
class PopularViewModel(
    repository: MovieRepository = MovieRepository()
) : ViewModel() {

    // cachedIn guarda las paginas ya cargadas en el viewmodel
    // asi al rotar la pantalla no se vuelve a pedir todo y se mantiene el scroll
    val movies: Flow<PagingData<Movie>> = repository.getPopularMovies()
        .cachedIn(viewModelScope)
}
