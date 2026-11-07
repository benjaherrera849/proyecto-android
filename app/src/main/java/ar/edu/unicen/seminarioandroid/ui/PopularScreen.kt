package ar.edu.unicen.seminarioandroid.ui


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ar.edu.unicen.seminarioandroid.R
import ar.edu.unicen.seminarioandroid.data.Movie
import ar.edu.unicen.seminarioandroid.data.RetrofitClient
import coil.compose.AsyncImage

// pantalla principal, decide que mostrar segun el estado del viewmodel
// onMovieClick le avisa a quien la use que se toco una pelicula (despues lo usamos para navegar)
@Composable
fun PopularScreen(
    onMovieClick: (Int) -> Unit,
    viewModel: PopularViewModel = viewModel()
) {
    // collectAsState convierte el stateflow en un state de compose
    // cuando el estado cambia, esta funcion se vuelve a ejecutar y la ui se redibuja
    val state by viewModel.state.collectAsState()

    // el when cubre los tres estados posibles
    when (val current = state) {
        is UiState.Loading -> LoadingIndicator()
        is UiState.Error -> ErrorMessage(
            message = stringResource(R.string.error_generic),
            onRetry = viewModel::loadMovies
        )
        is UiState.Success -> MovieList(
            movies = current.data,
            onMovieClick = onMovieClick
        )
    }
}

// lista vertical de peliculas
// lazycolumn solo dibuja los items que se ven en pantalla (como el recyclerview de xml)
@Composable
private fun MovieList(movies: List<Movie>, onMovieClick: (Int) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // key = id ayuda a compose a identificar cada item aunque cambie la lista
        items(movies, key = { it.id }) { movie ->
            MovieItem(movie = movie, onClick = { onMovieClick(movie.id) })
        }
    }
}

// una fila de la lista: imagen a la izquierda y titulo a la derecha
@Composable
private fun MovieItem(movie: Movie, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                // si la pelicula no tiene poster el model es null y coil no muestra nada
                model = movie.posterPath?.let { RetrofitClient.IMAGE_BASE_URL + it },
                contentDescription = movie.title,
                modifier = Modifier.width(80.dp).height(120.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}