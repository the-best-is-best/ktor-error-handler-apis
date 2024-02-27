package io.tbib.ktorerrorhandleapis

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

class InitErrorMessage {
    companion object {
        internal var jsonConvertError = "Error please contact with support"
        internal var timeOutError = "Request time out, try again later"
        internal var notFound = "The requested resource was not found."
        internal var unAuthorizd = "Please re login again"
        internal var badRequest = "Bad request. try again later"
        internal var forbiddenRequest = "Forbidden request. try again later"
        internal var internalServerError = "Something went wrong on the server side."
        internal var defaultError = "An unknown error occurred."
        internal var noInternetConnection = "No internet connection."
        internal var unknownError = "An unknown error occurred."


    }

    fun init(
        jsonConvertError: String? = null,
        unAuthorizd: String? = null,
        timeOutError: String? = null,
        notFound: String? = null,
        badRequest: String? = null,
        forbiddenRequest: String? = null,
        internalServerError: String? = null,
        defaultError: String?,
        noInternetConnection: String? = null,
        unknownError: String? = null
    ) {
        Companion.jsonConvertError = jsonConvertError ?: Companion.jsonConvertError
        Companion.timeOutError = timeOutError ?: Companion.timeOutError
        Companion.notFound = notFound ?: Companion.notFound
        Companion.badRequest = badRequest ?: Companion.badRequest
        Companion.forbiddenRequest = forbiddenRequest ?: Companion.forbiddenRequest
        Companion.internalServerError =
            internalServerError ?: Companion.internalServerError
        Companion.defaultError = defaultError ?: Companion.defaultError
        Companion.noInternetConnection =
            noInternetConnection ?: Companion.noInternetConnection
        Companion.unknownError = unknownError ?: Companion.unknownError
        Companion.unAuthorizd = unAuthorizd ?: Companion.unAuthorizd


    }
}


class KtorErrorHandler {
    suspend fun handle(e: Any): Failure {
        when (e) {
            is ClientRequestException -> {
                val statusCode = e.response.status.value
                val message = when (statusCode) {
                    HttpStatusCode.NotFound.value -> InitErrorMessage.notFound
                    HttpStatusCode.Unauthorized.value -> InitErrorMessage.unAuthorizd
                    HttpStatusCode.InternalServerError.value -> InitErrorMessage.internalServerError
                    HttpStatusCode.BadRequest.value -> InitErrorMessage.badRequest
                    HttpStatusCode.Forbidden.value -> InitErrorMessage.forbiddenRequest
                    HttpStatusCode.NoContent.value -> "No content."
                    HttpStatusCode.BadGateway.value -> "Bad gateway."
                    HttpStatusCode.GatewayTimeout.value -> "Gateway timeout."
                    HttpStatusCode.ServiceUnavailable.value -> "Service unavailable."
                    HttpStatusCode.RequestTimeout.value -> InitErrorMessage.timeOutError
                    HttpStatusCode.Conflict.value -> "Conflict."
                    HttpStatusCode.UnsupportedMediaType.value -> "Unsupported media type."
                    HttpStatusCode.NotImplemented.value -> "Not implemented."
                    HttpStatusCode.Gone.value -> "The requested resource is no longer available."
                    HttpStatusCode.UpgradeRequired.value -> "Upgrade required."
                    HttpStatusCode.TooManyRequests.value -> "Too many requests."
                    HttpStatusCode.RequestURITooLong.value -> "Request URI too long."

                    else -> InitErrorMessage.defaultError
                }
                val bodyString = e.response.bodyAsText()
                val jsonObject = Json.decodeFromString(JsonObject.serializer(),bodyString)
                return Failure(
                    statusCode = HttpStatusCode.fromValue(statusCode).value,
                    messages = message.ifBlank { jsonObject["message"].toString() }
                )
            }

            is SerializationException, is JsonConvertException -> {
                return Failure(messages = InitErrorMessage.jsonConvertError, statusCode = 1)

            }


            is IOException, is Throwable -> {
                return Failure(messages = InitErrorMessage.unknownError, statusCode = 1)

            }
        }
        return Failure(messages = InitErrorMessage.unknownError, statusCode = 1)
    }


}