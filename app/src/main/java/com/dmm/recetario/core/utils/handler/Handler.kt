package com.dmm.recetario.core.utils.handler

import com.dmm.recetario.R
import com.dmm.recetario.core.utils.helper.ResourceHelper
import com.dmm.recetario.domain.exception.APIException
import retrofit2.Response
import java.io.IOException

suspend fun <T> handleApiCall (
    resourceHelper: ResourceHelper,
    call: suspend () -> Response<T>
): T? {
    val getString: (Int) -> String = resourceHelper::getString

    return try {
        val response = call()

        if (response.isSuccessful) {
            response.body()
        } else {
            when (val code = response.code()) {
                400 -> throw APIException.BadRequestException (
                    getString(R.string.error_400)
                )
                401 -> throw APIException.UnauthorizedException (
                    getString(R.string.error_401)
                )
                403 -> throw APIException.ForbiddenException (
                    getString(R.string.error_403)
                )
                404 -> throw APIException.NotFoundException (
                    getString(R.string.error_404)
                )
                422 -> throw APIException.UnprocessableEntityException (
                    getString(R.string.error_422)
                )
                429 -> throw APIException.TooManyRequestsException (
                    getString(R.string.error_429)
                )

                500 -> throw APIException.ServerException (
                    getString(R.string.error_500)
                )

                else -> throw APIException.UnknownException (
                   "${getString(R.string.unknown_error)}: $code"
                )
            }
        }
    } catch (e: IOException) {
        throw APIException.NetworkException (
            e.message ?: getString(R.string.network_error)
        )
    } catch (e: Exception) {
        throw APIException.UnknownException (
            e.message ?: getString(R.string.unknown_error)
        )
    }
}