package edu.sspu.am

import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import aquamaster.composeapp.generated.resources.HarmonyOS_Sans_SC_Black
import aquamaster.composeapp.generated.resources.HarmonyOS_Sans_SC_Bold
import aquamaster.composeapp.generated.resources.HarmonyOS_Sans_SC_Medium
import aquamaster.composeapp.generated.resources.HarmonyOS_Sans_SC_Regular
import aquamaster.composeapp.generated.resources.Res
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.Font


expect fun log(message: String)


data class Page(
    val id: String,
    val title: String? = null,
    val icon: ImageVector? = null,
    val url: String? = null,
) {
    infix fun equal(page: Page): Boolean = page.id == id

    infix fun equal(page: String): Boolean = page == id

    override fun toString(): String = id
}

class Screen(
    private val pages: List<Page> = listOf(MetaPages.app)
) {
    val size
        get() = pages.size

    val page: Page
        get() = pages.last()

    val father
        get() = if (pages.size > 1) Screen(pages.dropLast(1)) else null

    val meta
        get() = pages.first()

    fun child(page: Page): Screen {
        val new = pages.toMutableList()
        new.add(page)
        return Screen(new.toList())
    }

    fun isChild(screen: Screen): Boolean {
        var ids = ""
        pages.forEachIndexed { index, page ->
            if (index != 0) {
                ids += "."
            }
            ids += page.id
            if (screen.pages.joinToString(".") { it.id } == ids) return true
        }
        return false
    }

    fun isChild(path: String): Boolean {
        var ids = ""
        pages.forEachIndexed { index, page ->
            if (index != 0) {
                ids += "."
            }
            ids += page.id
            if (path == ids) return true
        }
        return false
    }

    inline infix fun isChildOf(screen: Screen) = isChild(screen)

    inline infix fun isChildOf(path: String) = isChild(path)

    infix fun equal(screen: Screen) = screen.pages.joinToString(".") { it.id } == pages.joinToString(".") { it.id }

    infix fun equal(path: String) = path == pages.joinToString(".") { it.id }

    override fun toString(): String = pages.joinToString(".") { it.id }
}

inline fun makeScreen(path: String): Screen = Screen(path.split(".").map { Page(it) })

data class ScreenPathSyntaxCheckResult(
    val valid: Boolean,
    val message: String? = null,
)

fun checkScreenPathSyntax(path: String): ScreenPathSyntaxCheckResult {
    if (path.isBlank()) {
        return ScreenPathSyntaxCheckResult(false, "错误:路径不能为空")
    }
    val screen = makeScreen(path)
    if (screen.size == 0) {
        return ScreenPathSyntaxCheckResult(false, "错误:路径页面数量不可为0")
    }
    when {
        screen.meta.equal(MetaPages.app) -> {}

        screen.meta.equal(MetaPages.exit) -> {
            return ScreenPathSyntaxCheckResult(false, "错误:不能直接访问退出界面")
        }

        screen.meta.equal(MetaPages.splash) -> {
            return ScreenPathSyntaxCheckResult(false, "错误:不能访问启动界面")
        }

        screen.meta.equal(MetaPages.surprise) -> {
            return ScreenPathSyntaxCheckResult(true, "恭喜你找到了彩蛋")
        }

        else -> {
            return ScreenPathSyntaxCheckResult(false, "错误:MetaPage不合法, 不存在MetaPage{${screen.meta.id}}")
        }
    }
    return ScreenPathSyntaxCheckResult(true)
}


enum class DeviceType {
    MobilePhone,
    Desktop,
    Unknown
}

object DataStore {
    @Serializable
    data class Settings(
        val screen: String = MetaScreens.app.toString(),
        val theme: Themes = Themes.Light,
    )

    @Serializable
    data class MachineSettings(
        val current: MachineData? = null,
        val list:  List<MachineCategory> = listOf(),
    )
}

expect fun saveSettingsData(data: DataStore.Settings)

expect fun loadSettingsData(): DataStore.Settings

expect fun saveMachineSettingsData(data: DataStore.MachineSettings)

expect fun loadMachineSettingsData(): DataStore.MachineSettings

class UI : ViewModel() {
    /**
     * 追踪界面状态
     */

    fun init() {
        load()
    }

    @Composable
    fun initUI() {
        scope = rememberCoroutineScope()
        snack = remember { SnackbarHostState() }

        initDataStore()
        initFont()
        initTheme()
    }

    private fun save(
        settingsData: DataStore.Settings,
        machineSettingsData: DataStore.MachineSettings
    ) {
        saveSettingsData(settingsData)
        saveMachineSettingsData(machineSettingsData)
    }

    private fun load() {
        val settingsData = loadSettingsData()
        this goto settingsData.screen
        settings.theme set NameToTheme[settingsData.theme]!!

        val machineSettingsData = loadMachineSettingsData()
        if (machineSettingsData.current != null) {
            settings.machine.set(machineSettingsData.current)
        }
        data.list set machineSettingsData.list
    }

    private val _device = MutableStateFlow(DeviceType.Unknown)
    val device = _device.asStateFlow()

    infix fun setDevice(value: DeviceType) {
        _device.value = value
    }

    private val _screen = MutableStateFlow(Screen())
    val screen = _screen.asStateFlow()

    fun setScreen(screen: Screen) {
        _screen.value = screen
    }

    fun setScreen(path: String) {
        _screen.value = makeScreen(path)
    }

    infix fun goto(screen: Screen) = setScreen(screen)

    infix fun goto(path: String) = setScreen(path)

    inline infix fun matchUnComposable(value: Screen) = setOf(
        screen.value equal value,
        setOf(
            (screen.value equal MetaScreens.app),
            (settings.first.current.value equal value)
        ).all { it }
    ).any { it }

    inline infix fun matchUnComposable(value: String) = setOf(
        screen.value equal value,
        setOf(
            (screen.value equal MetaScreens.app),
            (settings.first.current.value equal value)
        ).all { it }
    ).any { it }

    @Composable
    inline infix fun match(value: Screen): Boolean {
        val screen by screen.collectAsState()
        val first by settings.first.current.collectAsState()
        return setOf(
            screen equal value,
            setOf(
                (screen equal MetaScreens.app),
                (first equal value)
            ).all { it }
        ).any { it }
    }

    @Composable
    inline infix fun match(value: String): Boolean {
        val screen by screen.collectAsState()
        val first by settings.first.current.collectAsState()
        return setOf(
            screen equal value,
            setOf(
                (screen equal MetaScreens.app),
                (first equal value)
            ).all { it }
        ).any { it }
    }

    inline infix fun childMatchUnComposable(value: Screen) = setOf(
        screen.value isChildOf value,
        setOf(
            (screen.value equal MetaScreens.app),
            (settings.first.current.value isChildOf value)
        ).all { it }
    ).any { it }

    inline infix fun childMatchUnComposable(value: String) = setOf(
        screen.value isChildOf value,
        setOf(
            (screen.value equal MetaScreens.app),
            (settings.first.current.value isChildOf value)
        ).all { it }
    ).any { it }

    @Composable
    inline infix fun childMatch(value: Screen): Boolean {
        val screen by screen.collectAsState()
        val first by settings.first.current.collectAsState()
        return setOf(
            screen isChildOf value,
            setOf(
                (screen equal MetaScreens.app),
                (first isChildOf value)
            ).all { it }
        ).any { it }
    }

    @Composable
    inline infix fun childMatch(value: String): Boolean {
        val screen by screen.collectAsState()
        val first by settings.first.current.collectAsState()
        return setOf(
            screen isChildOf value,
            setOf(
                (screen equal MetaScreens.app),
                (first isChildOf value)
            ).all { it }
        ).any { it }
    }

    inline infix fun pageMatchUnComposable(value: Page) = setOf(
        screen.value.page equal value,
        setOf(
            (screen.value equal MetaScreens.app),
            (settings.first.current.value.page equal value)
        ).all { it }
    ).any { it }

    inline infix fun pageMatchUnComposable(value: String) = setOf(
        screen.value.page equal value,
        setOf(
            (screen.value equal MetaScreens.app),
            (settings.first.current.value.page equal value)
        ).all { it }
    ).any { it }

    @Composable
    inline infix fun pageMatch(value: Page): Boolean {
        val screen by screen.collectAsState()
        val first by settings.first.current.collectAsState()
        return setOf(
            screen.page equal value,
            setOf(
                (screen equal MetaScreens.app),
                (first.page equal value)
            ).all { it }
        ).any { it }
    }

    @Composable
    inline infix fun pageMatch(value: String): Boolean {
        val screen by screen.collectAsState()
        val first by settings.first.current.collectAsState()
        return setOf(
            screen.page equal value,
            setOf(
                (screen equal MetaScreens.app),
                (first.page equal value)
            ).all { it }
        ).any { it }
    }

    fun back() {
        if (settings.ai.enable.value) {
            settings.ai.off()
        } else if (screen.value.father == null) {
            if ((screen.value.meta == MetaPages.app) or (screen.value.meta == MetaPages.splash)) {
                _screen.value = MetaScreens.exit
            } else {
                _screen.value = MetaScreens.app
            }
        } else if ((screen.value == settings.first.current.value) or (screen.value.meta == MetaPages.splash)) {
            _screen.value = MetaScreens.exit
        } else {
            _screen.value = screen.value.father!!
        }
    }

    lateinit var scope: CoroutineScope
    lateinit var snack: SnackbarHostState

    private val _theme = MutableStateFlow(ColorSchemes.light)
    val theme = _theme.asStateFlow()

    private val _font = MutableStateFlow<FontFamily>(FontFamily.Default)
    val font = _font.asStateFlow()

    val settings = Settings

    object Settings {
        val theme = ColorScheme

        object ColorScheme {
            private val _current = MutableStateFlow(
                Theme(
                    text = "日间",
                    theme = Themes.Light,
                    icon = Icons.Outlined.LightMode,
                    iconFilled = Icons.Filled.LightMode
                )
            )
            val current = _current.asStateFlow()

            infix fun set(theme: Theme) {
                _current.value = theme
            }
        }

        val first = CustomFirstScreen

        object CustomFirstScreen {
            private val _current = MutableStateFlow(RootScreens.List)
            val current = _current.asStateFlow()

            infix fun set(screen: Screen) {
                _current.value = screen
            }

            infix fun set(path: String) {
                _current.value = makeScreen(path)
            }
        }

        val developerMode = DeveloperMode

        object DeveloperMode {
            private val _enable = MutableStateFlow(false)
            val enable = _enable.asStateFlow()

            val developerItems = DeveloperItems

            object DeveloperItems {
                val screenIndicator = ScreenIndicator

                object ScreenIndicator {
                    private val _visible = MutableStateFlow(false)
                    val visible = _visible.asStateFlow()

                    fun switch() {
                        _visible.value = !_visible.value
                    }

                    infix fun set(value: Boolean) {
                        _visible.value = value
                    }
                }
            }

            val screenJumper = ScreenJumper

            object ScreenJumper {
                private val _visible = MutableStateFlow(false)
                val visible = _visible.asStateFlow()

                fun switch() {
                    _visible.value = !_visible.value
                }
            }

            val fastJumper = FastJumper

            object FastJumper {
                enum class FastJumperMode {
                    Screen, WebUrl, TinySoftware, Command
                }

                val one = One

                object One {
                    val target = Target

                    object Target {
                        private val _current = MutableStateFlow(RootScreens.List.toString())
                        val current = _current.asStateFlow()

                        infix fun set(value: String) {
                            _current.value = value
                        }
                    }

                    val mode = Mode

                    object Mode {
                        private val _current = MutableStateFlow(FastJumperMode.Screen)
                        val current = _current.asStateFlow()

                        infix fun set(value: FastJumperMode) {
                            _current.value = value
                        }
                    }
                }

                val two = Two

                object Two {
                    val target = Target

                    object Target {
                        private val _current = MutableStateFlow(RootScreens.Cloud.toString())
                        val current = _current.asStateFlow()

                        infix fun set(value: String) {
                            _current.value = value
                        }
                    }

                    val mode = Mode

                    object Mode {
                        private val _current = MutableStateFlow(FastJumperMode.Screen)
                        val current = _current.asStateFlow()

                        infix fun set(value: FastJumperMode) {
                            _current.value = value
                        }
                    }
                }

                val three = Three

                object Three {
                    val target = Target

                    object Target {
                        private val _current = MutableStateFlow(RootScreens.Community.toString())
                        val current = _current.asStateFlow()

                        infix fun set(value: String) {
                            _current.value = value
                        }
                    }

                    val mode = Mode

                    object Mode {
                        private val _current = MutableStateFlow(FastJumperMode.Screen)
                        val current = _current.asStateFlow()

                        infix fun set(value: FastJumperMode) {
                            _current.value = value
                        }
                    }
                }

                val four = Four

                object Four {
                    val target = Target

                    object Target {
                        private val _current = MutableStateFlow(RootScreens.Personal.toString())
                        val current = _current.asStateFlow()

                        infix fun set(value: String) {
                            _current.value = value
                        }
                    }

                    val mode = Mode

                    object Mode {
                        private val _current = MutableStateFlow(FastJumperMode.Screen)
                        val current = _current.asStateFlow()

                        infix fun set(value: FastJumperMode) {
                            _current.value = value
                        }
                    }
                }
            }

            fun switch() {
                _enable.value = !_enable.value
            }

            fun on() {
                _enable.value = true
            }

            fun off() {
                _enable.value = false
            }

            infix fun set(enable: Boolean) {
                _enable.value = enable
            }
        }

        val home = Home

        object Home

        val ai = AI

        object AI {
            private val _enable = MutableStateFlow(false)
            val enable = _enable.asStateFlow()

            fun on() {
                _enable.value = true
            }

            fun off() {
                _enable.value = false
            }

            fun switch() {
                _enable.value = !_enable.value
            }
        }

        val machine = MachineSettings

        object MachineSettings {
            private val _current = MutableStateFlow<MachineData?>(null)
            val current = _current.asStateFlow()

            fun set(machine: MachineData) {
                _current.value = machine
            }

            val refreshing = Refreshing

            object Refreshing {
                private val _enable = MutableStateFlow(false)
                val enable = _enable.asStateFlow()

                fun on() {
                    _enable.value = true
                }

                fun off() {
                    _enable.value = false
                }

                fun switch() {
                    _enable.value = !_enable.value
                }
            }

            val ai = MachineAISettings

            object MachineAISettings {
                val operateWhenAlarm = AIOperateWhenAlarm

                object AIOperateWhenAlarm {
                    private val _enable = MutableStateFlow(false)
                    val enable = _enable.asStateFlow()

                    fun on() {
                        _enable.value = true
                    }

                    fun off() {
                        _enable.value = false
                    }

                    fun switch() {
                        _enable.value = !_enable.value
                    }
                }

                val operateWhenWarning = AIOperateWhenWarning

                object AIOperateWhenWarning {
                    private val _enable = MutableStateFlow(false)
                    val enable = _enable.asStateFlow()

                    fun on() {
                        _enable.value = true
                    }

                    fun off() {
                        _enable.value = false
                    }

                    fun switch() {
                        _enable.value = !_enable.value
                    }
                }
            }


            val running = RunningData

            object RunningData {
                val limit = RunningLimit

                object RunningLimit {
                    val alarm = AlarmTo

                    object AlarmTo {
                        val administer = AlarmToAdminister

                        object AlarmToAdminister {
                            val air = AlarmToAdministerAirLimit

                            object AlarmToAdministerAirLimit {
                                val temperature = AlarmToAdministerAirLimitTemperature

                                object AlarmToAdministerAirLimitTemperature {
                                    val lower = AlarmToAdministerAirLimitTemperatureLower

                                    object AlarmToAdministerAirLimitTemperatureLower {
                                        private val _current = MutableStateFlow(0)
                                        val current = _current.asStateFlow()

                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }

                                    val upper = AlarmToAdministerAirLimitTemperatureUpper

                                    object AlarmToAdministerAirLimitTemperatureUpper {
                                        private val _current = MutableStateFlow(100)
                                        val current = _current.asStateFlow()

                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }
                                }

                                val humidity = AlarmToAdministerAirLimitHumidity

                                object AlarmToAdministerAirLimitHumidity {
                                    val lower = AlarmToAdministerAirLimitHumidityLower

                                    object AlarmToAdministerAirLimitHumidityLower {
                                        private val _current = MutableStateFlow(0)
                                        val current = _current.asStateFlow()

                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }

                                    val upper = AlarmToAdministerAirLimitHumidityUpper

                                    object AlarmToAdministerAirLimitHumidityUpper {
                                        private val _current = MutableStateFlow(100)
                                        val current = _current.asStateFlow()
                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }
                                }
                            }

                            val soil = AlarmToAdministerSoilLimit

                            object AlarmToAdministerSoilLimit {
                                val temperature = AlarmToAdministerSoilLimitTemperature

                                object AlarmToAdministerSoilLimitTemperature {
                                    val lower = AlarmToAdministerSoilLimitTemperatureLower

                                    object AlarmToAdministerSoilLimitTemperatureLower {
                                        private val _current = MutableStateFlow(0)
                                        val current = _current.asStateFlow()

                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }

                                    val upper = AlarmToAdministerSoilLimitTemperatureUpper

                                    object AlarmToAdministerSoilLimitTemperatureUpper {
                                        private val _current = MutableStateFlow(100)
                                        val current = _current.asStateFlow()

                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }
                                }

                                val humidity = AlarmToAdministerSoilLimitHumidity

                                object AlarmToAdministerSoilLimitHumidity {
                                    val lower = AlarmToAdministerSoilLimitHumidityLower

                                    object AlarmToAdministerSoilLimitHumidityLower {
                                        private val _current = MutableStateFlow(0)
                                        val current = _current.asStateFlow()
                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }

                                    val upper = AlarmToAdministerSoilLimitHumidityUpper

                                    object AlarmToAdministerSoilLimitHumidityUpper {
                                        private val _current = MutableStateFlow(100)
                                        val current = _current.asStateFlow()
                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }
                                }
                            }
                        }

                        val ai = AlarmToAI

                        object AlarmToAI {
                            val air = AlarmToAIAirLimit

                            object AlarmToAIAirLimit {
                                val temperature = AlarmToAIAirLimitTemperature

                                object AlarmToAIAirLimitTemperature {
                                    val lower = AlarmToAIAirLimitTemperatureLower

                                    object AlarmToAIAirLimitTemperatureLower {
                                        private val _current = MutableStateFlow(0)
                                        val current = _current.asStateFlow()

                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }

                                    val upper = AlarmToAIAirLimitTemperatureUpper

                                    object AlarmToAIAirLimitTemperatureUpper {
                                        private val _current = MutableStateFlow(100)
                                        val current = _current.asStateFlow()

                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }
                                }

                                val humidity = AlarmToAIAirLimitHumidity

                                object AlarmToAIAirLimitHumidity {
                                    val lower = AlarmToAIAirLimitHumidityLower

                                    object AlarmToAIAirLimitHumidityLower {
                                        private val _current = MutableStateFlow(0)
                                        val current = _current.asStateFlow()
                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }

                                    val upper = AlarmToAIAirLimitHumidityUpper

                                    object AlarmToAIAirLimitHumidityUpper {
                                        private val _current = MutableStateFlow(100)
                                        val current = _current.asStateFlow()
                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }
                                }
                            }

                            val soil = AlarmToAISoilLimit

                            object AlarmToAISoilLimit {
                                val temperature = AlarmToAISoilLimitTemperature

                                object AlarmToAISoilLimitTemperature {
                                    val lower = AlarmToAISoilLimitTemperatureLower

                                    object AlarmToAISoilLimitTemperatureLower {
                                        private val _current = MutableStateFlow(0)
                                        val current = _current.asStateFlow()

                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }

                                    val upper = AlarmToAISoilLimitTemperatureUpper

                                    object AlarmToAISoilLimitTemperatureUpper {
                                        private val _current = MutableStateFlow(100)
                                        val current = _current.asStateFlow()

                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }
                                }

                                val humidity = AlarmToAISoilLimitHumidity

                                object AlarmToAISoilLimitHumidity {
                                    val lower = AlarmToAISoilLimitHumidityLower

                                    object AlarmToAISoilLimitHumidityLower {
                                        private val _current = MutableStateFlow(0)
                                        val current = _current.asStateFlow()
                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }

                                    val upper = AlarmToAISoilLimitHumidityUpper

                                    object AlarmToAISoilLimitHumidityUpper {
                                        private val _current = MutableStateFlow(100)
                                        val current = _current.asStateFlow()
                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }
                                }
                            }
                        }
                    }

                    val warning = WarningTo

                    object WarningTo {
                        val administer = WarningToAdminister

                        object WarningToAdminister {
                            val air = WarningToAdministerAirLimit

                            object WarningToAdministerAirLimit {
                                val temperature = WarningToAdministerAirLimitTemperature

                                object WarningToAdministerAirLimitTemperature {
                                    val lower = WarningToAdministerAirLimitTemperatureLower

                                    object WarningToAdministerAirLimitTemperatureLower {
                                        private val _current = MutableStateFlow(0)
                                        val current = _current.asStateFlow()
                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }

                                    val upper = WarningToAdministerAirLimitTemperatureUpper

                                    object WarningToAdministerAirLimitTemperatureUpper {
                                        private val _current = MutableStateFlow(100)
                                        val current = _current.asStateFlow()
                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }
                                }

                                val humidity = WarningToAdministerAirLimitHumidity

                                object WarningToAdministerAirLimitHumidity {
                                    val lower = WarningToAdministerAirLimitHumidityLower

                                    object WarningToAdministerAirLimitHumidityLower {
                                        private val _current = MutableStateFlow(0)
                                        val current = _current.asStateFlow()
                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }

                                    val upper = WarningToAdministerAirLimitHumidityUpper

                                    object WarningToAdministerAirLimitHumidityUpper {
                                        private val _current = MutableStateFlow(100)
                                        val current = _current.asStateFlow()
                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }
                                }
                            }

                            val soil = WarningToAdministerSoilLimit

                            object WarningToAdministerSoilLimit {
                                val temperature = WarningToAdministerSoilLimitTemperature

                                object WarningToAdministerSoilLimitTemperature {
                                    val lower = WarningToAdministerSoilLimitTemperatureLower

                                    object WarningToAdministerSoilLimitTemperatureLower {
                                        private val _current = MutableStateFlow(0)
                                        val current = _current.asStateFlow()
                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }

                                    val upper = WarningToAdministerSoilLimitTemperatureUpper

                                    object WarningToAdministerSoilLimitTemperatureUpper {
                                        private val _current = MutableStateFlow(100)
                                        val current = _current.asStateFlow()
                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }
                                }

                                val humidity = WarningToAdministerSoilLimitHumidity

                                object WarningToAdministerSoilLimitHumidity {
                                    val lower = WarningToAISoilLimitHumidityLower

                                    object WarningToAISoilLimitHumidityLower {
                                        private val _current = MutableStateFlow(0)
                                        val current = _current.asStateFlow()
                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }

                                    val upper = WarningToAISoilLimitHumidityUpper

                                    object WarningToAISoilLimitHumidityUpper {
                                        private val _current = MutableStateFlow(100)
                                        val current = _current.asStateFlow()
                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }
                                }
                            }
                        }

                        val ai = WarningToAI

                        object WarningToAI {
                            val air = WarningToAIAirLimit

                            object WarningToAIAirLimit {
                                val temperature = WarningToAIAirLimitTemperature

                                object WarningToAIAirLimitTemperature {
                                    val lower = WarningToAIAirLimitTemperatureLower

                                    object WarningToAIAirLimitTemperatureLower {
                                        private val _current = MutableStateFlow(0)
                                        val current = _current.asStateFlow()
                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }

                                    val upper = WarningToAIAirLimitTemperatureUpper

                                    object WarningToAIAirLimitTemperatureUpper {
                                        private val _current = MutableStateFlow(100)
                                        val current = _current.asStateFlow()
                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }
                                }

                                val humidity = WarningToAIAirLimitHumidity

                                object WarningToAIAirLimitHumidity {
                                    val lower = WarningToAIAirLimitHumidityLower

                                    object WarningToAIAirLimitHumidityLower {
                                        private val _current = MutableStateFlow(0)
                                        val current = _current.asStateFlow()
                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }

                                    val upper = WarningToAIAirLimitHumidityUpper

                                    object WarningToAIAirLimitHumidityUpper {
                                        private val _current = MutableStateFlow(100)
                                        val current = _current.asStateFlow()
                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }
                                }
                            }

                            val soil = WarningToAISoilLimit

                            object WarningToAISoilLimit {
                                val temperature = WarningToAISoilLimitTemperature

                                object WarningToAISoilLimitTemperature {
                                    val lower = WarningToAISoilLimitTemperatureLower

                                    object WarningToAISoilLimitTemperatureLower {
                                        private val _current = MutableStateFlow(0)
                                        val current = _current.asStateFlow()
                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }

                                    val upper = WarningToAISoilLimitTemperatureUpper

                                    object WarningToAISoilLimitTemperatureUpper {
                                        private val _current = MutableStateFlow(100)
                                        val current = _current.asStateFlow()
                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }
                                }

                                val humidity = WarningToAISoilLimitHumidity

                                object WarningToAISoilLimitHumidity {
                                    val lower = WarningToAISoilLimitHumidityLower

                                    object WarningToAISoilLimitHumidityLower {
                                        private val _current = MutableStateFlow(0)
                                        val current = _current.asStateFlow()
                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }

                                    val upper = WarningToAISoilLimitHumidityUpper

                                    object WarningToAISoilLimitHumidityUpper {
                                        private val _current = MutableStateFlow(100)
                                        val current = _current.asStateFlow()
                                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                                            _current.value = value
                                        }

                                        fun increment() {
                                            if (current.value > 0) _current.value++
                                        }

                                        fun decrement() {
                                            if (current.value < 100) _current.value--
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                val power = RunningPower

                object RunningPower {
                    val wind = WindRunningPower

                    object WindRunningPower {
                        private val _current = MutableStateFlow(0)
                        val current = _current.asStateFlow()

                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                            _current.value = value
                        }

                        fun increment() {
                            if (current.value > 0) _current.value++
                        }

                        fun decrement() {
                            if (current.value < 100) _current.value--
                        }
                    }

                    val water = WaterRunningPower

                    object WaterRunningPower {
                        private val _current = MutableStateFlow(0)
                        val current = _current.asStateFlow()

                        infix fun set(@IntRange(from = 0, to = 100) value: Int) {
                            _current.value = value
                        }

                        fun increment() {
                            if (current.value > 0) _current.value++
                        }

                        fun decrement() {
                            if (current.value < 100) _current.value--
                        }
                    }
                }
            }

            val sensor = MachineSensor

            object MachineSensor {
                val air = MachineAirSensor

                object MachineAirSensor {
                    val temperature = MachineAirTemperatureData

                    object MachineAirTemperatureData {
                        private val _current = MutableStateFlow(0f)
                        val current = _current.asStateFlow()
                        infix fun set(@FloatRange(from = -273.15, to = 100.0) value: Float) {
                            _current.value = value
                        }
                    }

                    val humidity = MachineAirHumidityData

                    object MachineAirHumidityData {
                        private val _current = MutableStateFlow(0f)
                        val current = _current.asStateFlow()
                        infix fun set(@FloatRange(from = 0.0, to = 100.0) value: Float) {
                            _current.value = value
                        }
                    }
                }

                val soil = MachineSoilSensor

                object MachineSoilSensor {
                    val temperature = MachineSoilTemperatureData

                    object MachineSoilTemperatureData {
                        private val _current = MutableStateFlow(0f)
                        val current = _current.asStateFlow()
                        infix fun set(@FloatRange(from = -273.15, to = 100.0) value: Float) {
                            _current.value = value
                        }
                    }

                    val humidity = MachineSoilHumidityData

                    object MachineSoilHumidityData {
                        private val _current = MutableStateFlow(0f)
                        val current = _current.asStateFlow()
                        infix fun set(@FloatRange(from = 0.0, to = 100.0) value: Float) {
                            _current.value = value
                        }
                    }
                }
            }
        }

        val list = MachineList

        object MachineList {
            val refreshing = Refreshing

            object Refreshing {
                private val _enable = MutableStateFlow(false)
                val enable = _enable.asStateFlow()

                fun on() {
                    _enable.value = true
                }

                fun off() {
                    _enable.value = false
                }

                fun switch() {
                    _enable.value = !_enable.value
                }
            }
        }
    }

    val data = Data

    object Data {
        val list = MachineList

        object MachineList {
            private val _categories = MutableStateFlow<SnapshotStateList<MachineCategory>>(mutableStateListOf())
            val categories = _categories.asStateFlow()

            infix fun append(category: MachineCategory) {
                _categories.value.add(category)
            }

            infix fun remove(category: MachineCategory) {
                _categories.value.remove(category)
            }

            fun setValue(index: Int, category: MachineCategory) {
                _categories.value[index] = category
            }

            infix fun set(value: List<MachineCategory>) {
                _categories.value = value.toMutableStateList()
            }
        }
    }

    @Composable
    internal fun initFont() {
        _font.value = FontFamily(
            Font(Res.font.HarmonyOS_Sans_SC_Regular, FontWeight.Normal),
            Font(Res.font.HarmonyOS_Sans_SC_Medium, FontWeight.Medium),
            Font(Res.font.HarmonyOS_Sans_SC_Bold, FontWeight.Bold),
            Font(Res.font.HarmonyOS_Sans_SC_Black, FontWeight.Black),
        )
    }

    @Composable
    internal fun initTheme() {
        val currentTheme by settings.theme.current.collectAsState()

        _theme.value = when (currentTheme.theme) {
            Themes.Light -> ColorSchemes.light
            Themes.Dark -> ColorSchemes.dark
            Themes.Auto -> if (isSystemInDarkTheme()) ColorSchemes.dark else ColorSchemes.light
        }
    }


    @Composable
    internal fun initDataStore() {
        val currentScreen by screen.collectAsState()
        val currentTheme by settings.theme.current.collectAsState()

        val currentMachine by settings.machine.current.collectAsState()
        val currentMachineList by data.list.categories.collectAsState()

        save(
            settingsData = DataStore.Settings(
                screen = if (currentScreen equal MetaScreens.exit)
                    MetaScreens.app.toString()
                else
                    currentScreen.toString(),
                theme = currentTheme.theme
            ),
            machineSettingsData = DataStore.MachineSettings(
                current = currentMachine,
                list = currentMachineList.toList()
            )
        )
    }
}
