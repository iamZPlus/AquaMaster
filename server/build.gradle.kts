plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application

    alias(libs.plugins.kotlinxSerialization)
}

group = "edu.sspu.am"
version = "1.0.0"
application {
    mainClass.set("edu.sspu.am.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.websockets)

    implementation(libs.jetbrains.kotlinx.serialization.json)
    implementation(libs.alibaba.dashscope)
}