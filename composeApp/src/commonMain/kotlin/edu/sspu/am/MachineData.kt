package edu.sspu.am

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable


enum class MachineCategoryIcon {
    Star,
    Heart
}

val machineCategoryIconToImageVector: Map<MachineCategoryIcon, ImageVector> = mapOf(
    MachineCategoryIcon.Star to Icons.Default.Star,
    MachineCategoryIcon.Heart to Icons.Default.Favorite,
)

@Serializable
data class MachineCategory(
    val name: String? = null,
    val icon: MachineCategoryIcon? = null,
    val machines: List<MachineData> = listOf(),
    val unfold: Boolean = true,
) {
    fun change(
        name: String? = null,
        icon: MachineCategoryIcon? = null,
        machines: List<MachineData>? = null,
        unfold: Boolean? = null,
    ) = MachineCategory(
        name = name ?: this.name,
        icon = icon ?: this.icon,
        machines = machines ?: this.machines,
        unfold = unfold ?: this.unfold
    )
}

@Serializable
data class MachineData(
    val type: MachineType = MachineType.Blank,
    val name: String? = null,
    val url: MachineUrl? = null,
) {
    fun change(
        type: MachineType? = null,
        name: String? = null,
        url: MachineUrl? = null,
    ) = MachineData(
        type = type ?: this.type,
        name = name ?: this.name,
        url = url ?: this.url
    )
}


enum class MachineType {
    Local, // 本地设备
    Cloud, // 云端设备
    Remote, // Local + Cloud
    Blank, // 空设备
    Template, // 设备模板
    Group, // 设备组
    Virtual, // 虚拟设备
}

@Serializable
sealed class MachineUrl {
    open val url get() = ""
    open val display get() = ""

    @Serializable
    data class Local(
        val host: String,
        val port: Int
    ) : MachineUrl() {
        override val url get() = "http://$host:$port"
        override val display get() = "local://$host:$port"
    }

    @Serializable
    data class Cloud(
        val server: String,
        val id: String
    ) : MachineUrl() {
        override val url get() = "https://$server/m/$id"
        override val display get() = "cloud://$server/m/$id"
    }

    @Serializable
    data class Remote(
        val local: Local,
        val cloud: Cloud
    ) : MachineUrl() {
        override val url get() = "https://${cloud.server}/m/${cloud.id}"
        override val display get() = "remote://[${local.host}:${local.port}]to[${cloud.server}/m/${cloud.id}]"
    }

    @Serializable
    data object Blank : MachineUrl() {
        override val url get() = ""
        override val display get() = "Blank~"
    }

    @Serializable
    data class Template(
        val server: String,
        val id: String,
        val author: String
    ) : MachineUrl() {
        override val url get() = "https://$server/t/$author/$id"
        override val display get() = "template://$server/t/$author/$id"
    }

    @Serializable
    data class Group(
        val server: String,
        val id: String,
    ) : MachineUrl() {
        override val url get() = "https://$server/g/$id"
        override val display get() = "group://$server/g/$id"
    }

    // TODO: 正式上线时删除这部分
    @Serializable
    data object Virtual : MachineUrl() {
        override val url get() = ""
        override val display get() = "Virtual~"
    }
    // TODO: 虚拟设备只在软件测试时起作用
}


