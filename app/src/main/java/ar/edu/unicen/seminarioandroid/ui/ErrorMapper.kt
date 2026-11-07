package ar.edu.unicen.seminarioandroid.ui

import ar.edu.unicen.seminarioandroid.R
import retrofit2.HttpException
import java.io.IOException

// recibe la excepcion que guardamos en el estado error y devuelve el texto a mostrar
// devuelve el id del string (no el texto) para que se traduzca segun el idioma del celular
fun errorMessageRes(throwable: Throwable): Int {
    return when (throwable) {
        // ioexception cubre cuando no hay internet, no se llega al servidor o se agota el tiempo
        is IOException -> R.string.error_no_internet
        // httpexception es cuando el servidor si respondio pero con un codigo de error
        is HttpException -> R.string.error_server
        // cualquier otra cosa que no esperabamos
        else -> R.string.error_generic
    }
}