package edu.sspu.am

import io.ktor.client.*
import io.ktor.client.engine.cio.*

actual fun platformHttpClient(userConfig: HttpClientConfig<*>.() -> Unit): HttpClient  = HttpClient(CIO, userConfig)