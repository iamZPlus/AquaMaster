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

val settingsFileName = "$settingsHomeName\\.settings"
val settingsFile = File(settingsFileName)

val machineSettingsHomeName = "$dataHomeName\\machineSettings"
val machineSettingsHome = File(machineSettingsHomeName)

val machineSettingsFileName = "$machineSettingsHomeName\\.settings"
val machineSettingsFile = File(machineSettingsFileName)

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

actual fun saveMachineSettingsData(data: DataStore.MachineSettings) {
    if (!machineSettingsHome.exists()) {
        machineSettingsHome.mkdirs()
    }

    machineSettingsFile.writeText(Json.encodeToString(data))
}

actual fun loadMachineSettingsData(): DataStore.MachineSettings = Json.decodeFromString(
    try {
        machineSettingsFile.readText()
    } catch (e: IOException) {
        "{}"
    }
)