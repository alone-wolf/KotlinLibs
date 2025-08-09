package top.writerpass.kmplibrary.file

import java.awt.Desktop
import java.io.File
import java.io.IOException

object PathOpener {
    fun open(filePath: String): Boolean {
        val file = File(filePath)
        return try {
            if (!file.exists()) return false

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file)
                true
            } else {
                // Fallback for systems without java.awt.Desktop support
                val os = System.getProperty("os.name").lowercase()
                val process = when {
                    os.contains("win") -> ProcessBuilder(
                        "cmd",
                        "/c",
                        "start",
                        "\"\"",
                        file.absolutePath
                    )

                    os.contains("mac") -> ProcessBuilder("open", file.absolutePath)
                    os.contains("nix") || os.contains("nux") || os.contains("aix") -> ProcessBuilder(
                        "xdg-open",
                        file.absolutePath
                    )

                    else -> return false
                }.start()
                process.waitFor()
                process.exitValue() == 0
            }
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
}