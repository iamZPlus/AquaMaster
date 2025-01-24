package edu.sspu.am

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException
actual fun log(message: String) = println(message)

val userHomeName: String = System.getProperty("user.home")
val userHome = File(userHomeName)

val moHomeName = "$userHomeName\\.sspu"
val moHome = File(moHomeName)

val appHomeName = "$moHomeName\\AquaMaster"
val appHome = File(appHomeName)

val dataHomeName = "$appHomeName\\data"
val dataHome = File(dataHomeName)

val settingsHomeName = "$dataHomeName\\settings"
val settingsHome = File(settingsHomeName)

val settingsFileName = "$settingsHome\\.settings"
val settingsFile = File(settingsFileName)

actual fun saveSettingsData(data: DataStore.Settings) {
    if (!settingsHome.exists()) {
        settingsHome.mkdirs()
    }

    settingsFile.writeText(Json.encodeToString(data))
}

actual fun loadSettingsData(): DataStore.Settings = Json.decodeFromString(
    try {
        settingsFile.readText()
    } catch (e: IOException) {
        "{}"
    }
)