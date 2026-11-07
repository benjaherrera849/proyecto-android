package ar.edu.unicen.seminarioandroid.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unicen.seminarioandroid.data.Movie
import ar.edu.unicen.seminarioandroid.data.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// guarda el estado de la pantalla de populares y le pide los datos al repository
// al ser un viewmodel sobrevive a rotaciones de pantalla, no se vuelve a pedir todo
class PopularViewModel(
    private val repository: MovieRepository = MovieRepository()
) : ViewModel() {

    // _state es mutable y privado, solo el viewmodel puede cambiarlo
    // arranca en loading porque lo primero que hacemos es cargar
    private val _state = MutableStateFlow<UiState<List<Movie>>>(UiState.Loading)

    // la pantalla solo ve la version de solo lectura, asi no puede modificar el estado
    val state: StateFlow<UiState<List<Movie>>> = _state.asStateFlow()

    // apenas se crea el viewmodel cargamos las peliculas
    init {
        loadMovies()
    }

    // se usa al iniciar y tambien para el boton de reintentar
    fun loadMovies() {
        // ponemos loading de nuevo para que al reintentar se vea el indicador
        _state.value = UiState.Loading

        // la corrutina se pausa en la llamada de red sin bloquear el hilo principal
        viewModelScope.launch {
            try {
                _state.value = UiState.Success(repository.getPopularMovies())
            } catch (e: Exception) {
                // cualquier fallo (sin internet, error del servidor, etc) pasa a estado error
                _state.value = UiState.Error(e)
            }
        }
    }
}
