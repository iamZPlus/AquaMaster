package edu.sspu.am

import io.ktor.client.*
import io.ktor.client.engine.darwin.*

actual fun platformHttpClient(userConfig: HttpClientConfig<*>.() -> Unit): HttpClient = HttpClient(Darwin, userConfig)