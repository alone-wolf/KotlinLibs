package top.writerpass.qweather.sdk

import top.writerpass.resloader.ResourceLoader
import top.writerpass.resloader.read

object QWeatherIcons {
    fun icon(id: String, fill: Boolean = false): ByteArray {
        return if (fill) iconWithIdFilled(id) else iconWithId(id)
    }

    private fun iconWithIdFilled(id: String): ByteArray {
        val filename = "${id}-fill.svg"
        return ResourceLoader.read("files/qweather/icons/$filename")
    }

    private fun iconWithId(id: String): ByteArray {
        val filename = "${id}.svg"
        return ResourceLoader.read("files/qweather/icons/$filename")
    }
}