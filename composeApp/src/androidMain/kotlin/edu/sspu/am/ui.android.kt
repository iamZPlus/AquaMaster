package edu.sspu.am

import android.util.Log
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException

actual fun log(message: String) { Log.d("log", message) }

val appHome: File = MainActivity.context.filesDir
val dataHome = File(appHome, "data")
val settingsHome = File(dataHome, "settings")
val settingsFile = File(settingsHome, ".settings")


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