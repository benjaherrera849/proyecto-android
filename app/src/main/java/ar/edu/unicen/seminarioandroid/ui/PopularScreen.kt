package ar.edu.unicen.seminarioandroid.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import android.content.res.Configuration
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import ar.edu.unicen.seminarioandroid.R
import ar.edu.unicen.seminarioandroid.data.Movie
import ar.edu.unicen.seminarioandroid.data.RetrofitClient
import coil.compose.AsyncImage

@Composable
fun PopularScreen(
    onMovieClick: (Int) -> Unit,
    viewModel: PopularViewModel = viewModel()
) {
    // convierte el flow de paging en algo que lazycolumn entiende
    val movies = viewModel.movies.collectAsLazyPagingItems()

    // refresh es el estado de la carga inicial (la primera pagina)
    val refresh = movies.loadState.refresh

    when {
        // primera carga y todavia no hay nada para mostrar
        refresh is LoadState.Loading && movies.itemCount == 0 -> LoadingIndicator()
        // fallo la primera carga: error a pantalla completa con reintentar
        refresh is LoadState.Error && movies.itemCount == 0 -> ErrorMessage(
            message = stringResource(errorMessageRes(refresh.error)),
            onRetry = { movies.retry() }
        )
        else -> MovieList(movies = movies, onMovieClick = onMovieClick)
    }
}

@Composable
private fun MovieList(movies: LazyPagingItems<Movie>, onMovieClick: (Int) -> Unit) {
    // leemos la orientacion actual, al rotar se recrea la activity y esto se recalcula
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    // una columna en vertical (queda igual que la lista) y dos en horizontal
    val columns = if (isLandscape) 2 else 1

    // lazyverticalgrid es como lazycolumn pero con varias columnas, tambien dibuja solo lo visible
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(count = movies.itemCount, key = movies.itemKey { it.id }) { index ->
            val movie = movies[index]
            if (movie != null) {
                MovieItem(movie = movie, onClick = { onMovieClick(movie.id) })
            }
        }

        // el loading y el error de abajo ocupan todo el ancho (maxLineSpan = todas las columnas)
        // si no, quedarian metidos en una sola columna
        when (val append = movies.loadState.append) {
            is LoadState.Loading -> item(span = { GridItemSpan(maxLineSpan) }) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is LoadState.Error -> item(span = { GridItemSpan(maxLineSpan) }) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(errorMessageRes(append.error)))
                    Button(onClick = { movies.retry() }) {
                        Text(text = stringResource(R.string.retry))
                    }
                }
            }
            else -> {}
        }
    }
}

@Composable
private fun MovieItem(movie: Movie, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
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