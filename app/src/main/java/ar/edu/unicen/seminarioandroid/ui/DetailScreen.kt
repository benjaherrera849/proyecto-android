package ar.edu.unicen.seminarioandroid.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ar.edu.unicen.seminarioandroid.R
import ar.edu.unicen.seminarioandroid.data.MovieDetails
import ar.edu.unicen.seminarioandroid.data.RetrofitClient
import coil.compose.AsyncImage

// pantalla de detalle, misma idea que la de populares: solo refleja el estado del viewmodel
@Composable
fun DetailScreen(viewModel: DetailViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    when (val current = state) {
        is UiState.Loading -> LoadingIndicator()
        is UiState.Error -> ErrorMessage(
            message = stringResource(errorMessageRes(current.throwable)),
            onRetry = viewModel::loadDetails
        )
        is UiState.Success -> DetailContent(current.data)
    }
}

// contenido con los datos ya cargados
// verticalscroll para que se pueda scrollear si la sinopsis es larga
@Composable
private fun DetailContent(details: MovieDetails) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        AsyncImage(
            model = details.posterPath?.let { RetrofitClient.IMAGE_BASE_URL + it },
            contentDescription = details.title,
            modifier = Modifier.fillMaxWidth().height(400.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = details.title, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        // %.1f muestra un solo decimal en el rating
        Text(text = stringResource(R.string.rating, details.voteAverage))

        // los generos vienen en una lista, los unimos separados por coma
        Text(text = details.genres.joinToString(", ") { it.name })
        Spacer(modifier = Modifier.height(16.dp))

        // la sinopsis puede venir vacia, en ese caso no mostramos nada
        if (!details.overview.isNullOrBlank()) {
            Text(text = details.overview)
        }
    }
}