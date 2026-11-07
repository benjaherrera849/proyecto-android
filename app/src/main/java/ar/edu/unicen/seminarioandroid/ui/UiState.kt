package ar.edu.unicen.seminarioandroid.ui

// los tres estados posibles de una pantalla que carga datos de la red
// es generico (T) asi lo reusamos para la lista y para el detalle
// sealed = solo existen estos tres, y el when de la pantalla obliga a cubrirlos todos
sealed interface UiState<out T> {

    // esperando la respuesta, la pantalla muestra el indicador de carga
    data object Loading : UiState<Nothing>

    // llegaron los datos, la pantalla los muestra
    data class Success<T>(val data: T) : UiState<T>

    // fallo la consulta, guardamos la excepcion para despues distinguir sin internet de otros errores
    data class Error(val throwable: Throwable) : UiState<Nothing>
}


