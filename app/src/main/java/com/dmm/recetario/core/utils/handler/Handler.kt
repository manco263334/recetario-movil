package com.dmm.recetario.core.utils.handler

import com.dmm.recetario.domain.exception.APIException
import retrofit2.Response

suspend fun <T> handleApiCall(call: suspend () -> Response<T>): T? {
    return try {
        val response = call()
        if (response.isSuccessful) {
            response.body()
        } else {
            when (val code = response.code()) {
                400 -> throw APIException.BadRequestException("Solicitud incorrecta")
                401 -> throw APIException.UnauthorizedException("No tienes permiso")
                403 -> throw APIException.ForbiddenException("Prohibido")
                404 -> throw APIException.NotFoundException("No se encontró el recurso")
                422 -> throw APIException.UnprocessableEntityException("Entidad inprocesable")
                429 -> throw APIException.TooManyRequestsException("Demasiadas solicitudes")

                500 -> throw APIException.ServerException("El server explotó")

                else -> throw APIException.UnknownException("Error raro: $code")
            }
        }
    } catch (e: Exception) {
        // Errores de red o conexión
        throw APIException.UnknownException(e.message ?: "Error de red")
    }
}