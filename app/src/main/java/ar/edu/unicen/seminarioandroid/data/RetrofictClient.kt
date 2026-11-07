package ar.edu.unicen.seminarioandroid.data

import ar.edu.unicen.seminarioandroid.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

// object = singleton, asi hay una sola instancia de retrofit en toda la app
object RetrofitClient {

    // url base de las imagenes, despues se le agrega el posterPath de cada pelicula
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

    // el interceptor agrega el token y el idioma a todas las requests, asi no lo repetimos en cada endpoint
    // el token sale del buildconfig (se lee de local.properties) para no subirlo al repo

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()

            // agregamos ?language=xx a la url, tmdb responde en ese idioma
            // se lee en cada request (no una sola vez) por si el usuario cambia el idioma del celular
            val url = original.url.newBuilder()
                .addQueryParameter("language", Locale.getDefault().toLanguageTag())
                .build()

            val request = original.newBuilder()
                .url(url)
                .addHeader("Authorization", "Bearer ${BuildConfig.TMDB_TOKEN}")
                .build()
            chain.proceed(request)
        }
        .build()


    // armamos retrofit con la url base, el cliente de arriba y gson para parsear el json
    // by lazy no hace falta, se crea una vez cuando se usa por primera vez el object
    val api: TmdbApi = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TmdbApi::class.java)
}