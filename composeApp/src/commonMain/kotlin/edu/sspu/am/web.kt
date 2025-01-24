package edu.sspu.am

import io.ktor.client.*

expect fun platformHttpClient(userConfig: HttpClientConfig<*>.() -> Unit): HttpClient
