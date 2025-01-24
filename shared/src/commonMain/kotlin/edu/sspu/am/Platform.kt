package edu.sspu.am

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform