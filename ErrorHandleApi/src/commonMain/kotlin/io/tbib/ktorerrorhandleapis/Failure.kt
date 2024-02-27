package io.tbib.ktorerrorhandleapis

data class Failure(
    val statusCode: Int? = null,
    val messages: String? = null
)