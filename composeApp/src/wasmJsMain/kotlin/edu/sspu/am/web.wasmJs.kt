package edu.sspu.am

import io.ktor.client.*
import io.ktor.client.engine.js.*

actual fun platformHttpClient(userConfig: HttpClientConfig<*>.() -> Unit): HttpClient = HttpClient(Js, userConfig)