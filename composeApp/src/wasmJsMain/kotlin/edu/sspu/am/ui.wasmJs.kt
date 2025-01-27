package edu.sspu.am

import kotlinx.browser.localStorage
import kotlinx.serialization.json.Json

actual fun log(message: String) = println(message)
actual fun saveSettingsData(data: DataStore.Settings) {
    localStorage.setItem(
        key = "data.settings",
        value = Json.encodeToString(data)
    )
}

actual fun loadSettingsData(): DataStore.Settings = Json.decodeFromString(
    localStorage.getItem("data.settings") ?: "{}"
)

actual fun saveMachineSettingsData(data: DataStore.MachineSettings) {
    localStorage.setItem(
        key = "data.MachineSettings",
        value = Json.encodeToString(data)
    )
}

actual fun loadMachineSettingsData(): DataStore.MachineSettings = Json.decodeFromString(
    localStorage.getItem("data.MachineSettings") ?: "{}"
)