package ar.edu.unicen.seminarioandroid.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unicen.seminarioandroid.data.MovieDetails
import ar.edu.unicen.seminarioandroid.data.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// mismo patron que popularviewmodel pero para una sola pelicula
// el id de la pelicula llega por el savedstatehandle, que navigation completa con los argumentos de la ruta
class DetailViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val repository = MovieRepository()

    // si no llega el id es un error de programacion nuestro, por eso checkNotNull
    private val movieId: Int = checkNotNull(savedStateHandle.get<Int>("movieId"))

    private val _state = MutableStateFlow<UiState<MovieDetails>>(UiState.Loading)
    val state: StateFlow<UiState<MovieDetails>> = _state.asStateFlow()

    init {
        loadDetails()
    }

    // se usa al iniciar y para el boton de reintentar
    fun loadDetails() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            try {
                _state.value = UiState.Success(repository.getMovieDetails(movieId))
            } catch (e: Exception) {
                _state.value = UiState.Error(e)
            }
        }
    }
}