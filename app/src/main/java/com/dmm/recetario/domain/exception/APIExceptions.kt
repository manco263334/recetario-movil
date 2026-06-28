package com.dmm.recetario.domain.exception

sealed class APIException(message: String) : RuntimeException(message) {
    class BadRequestException(message: String) : APIException(message)
    class UnauthorizedException(message: String) : APIException(message)
    class ForbiddenException(message: String) : APIException(message)
    class NotFoundException(message: String) : APIException(message)
    class UnprocessableEntityException(message: String) : APIException(message)
    class TooManyRequestsException(message: String) : APIException(message)

    class ServerException(message: String) : APIException(message)

    class NetworkException(message: String) : APIException(message)
    class UnknownException(message: String) : APIException(message)
}